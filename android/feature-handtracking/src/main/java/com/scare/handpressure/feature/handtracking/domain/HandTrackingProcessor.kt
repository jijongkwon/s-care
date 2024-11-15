package com.scare.handpressure.feature.handtracking.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmark
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.scare.handpressure.feature.handtracking.util.ImageProcessingUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.ByteBuffer
import java.util.Optional
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class HandTrackingProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var handLandmarker: HandLandmarker? = null
    private var lastProcessedTimestamp = 0L
    private val MINIMUM_TIME_BETWEEN_PROCESSING = 33L // 30fps
    private val imageProcessingOptions = ImageProcessingUtils.createImageProcessingOptions()

    private val MINIMUM_HAND_DISTANCE = 0.15f // 두 손 사이의 최소 거리

    init {
        setupHandLandmarker()
    }

    private fun setupHandLandmarker() {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("models/hand_landmarker.task")
                .setDelegate(Delegate.GPU)
                .build()

            val options = HandLandmarker.HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinHandDetectionConfidence(0.25f)
                .setMinHandPresenceConfidence(0.25f)
                .setMinTrackingConfidence(0.25f)
                .setRunningMode(RunningMode.IMAGE)
                .setNumHands(2)
                .build()

            handLandmarker = HandLandmarker.createFromOptions(context, options)
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up HandLandmarker", e)
        }
    }

    private fun adjustHandPositions(landmarks: List<List<NormalizedLandmark>>): List<List<NormalizedLandmark>> {
        if (landmarks.size < 2) return landmarks

        val hand1Wrist = landmarks[0][HandLandmark.WRIST]
        val hand2Wrist = landmarks[1][HandLandmark.WRIST]
        val distance = abs(hand1Wrist.x() - hand2Wrist.x())

        // 두 손이 너무 가까운 경우만 조정
        if (distance < MINIMUM_HAND_DISTANCE) {
            val adjustmentNeeded = (MINIMUM_HAND_DISTANCE - distance) * -0.6f

            return landmarks.mapIndexed { index, handLandmarks ->
                val adjustment =
                    if (index == 0) adjustmentNeeded else -adjustmentNeeded
                handLandmarks.map { landmark ->
                    AdjustedLandmark(
                        x = landmark.x() + adjustment,
                        y = landmark.y(),
                        z = landmark.z()
                    )
                }
            }
        }

        return landmarks
    }

    private fun fixHandedness(result: HandLandmarkerResult): HandLandmarkerResult {
        if (result.handednesses().size < 2) return result

        val landmarks = result.landmarks()
        if (landmarks.size >= 2) {
            // 손의 위치 조정
            val adjustedLandmarks = adjustHandPositions(landmarks)

            // 손목 위치 대신 손바닥 중심점을 사용하여 좌우 판별
            val hand1Center = calculatePalmCenter(adjustedLandmarks[0])
            val hand2Center = calculatePalmCenter(adjustedLandmarks[1])

            // 추가로 손가락 방향도 고려
            val hand1Direction = calculateHandDirection(adjustedLandmarks[0])
            val hand2Direction = calculateHandDirection(adjustedLandmarks[1])

            // 손바닥 중심점과 손가락 방향을 모두 고려하여 좌우 결정
            val isHand1Right = (hand1Center > hand2Center) ||
                    (abs(hand1Center - hand2Center) < 0.1f && hand1Direction > hand2Direction)

            return if (isHand1Right) {
                SwappedHandLandmarkerResult(
                    originalResult = result,
                    landmarks = listOf(adjustedLandmarks[1], adjustedLandmarks[0]),
                    worldLandmarks = listOf(result.worldLandmarks()[1], result.worldLandmarks()[0]),
                    handednesses = listOf(result.handednesses()[1], result.handednesses()[0])
                )
            } else {
                SwappedHandLandmarkerResult(
                    originalResult = result,
                    landmarks = adjustedLandmarks,
                    worldLandmarks = result.worldLandmarks(),
                    handednesses = result.handednesses()
                )
            }
        }

        return result
    }

    // 손바닥 중심점 계산 (x 좌표의 평균)
    private fun calculatePalmCenter(landmarks: List<NormalizedLandmark>): Float {
        val palmLandmarks = listOf(0, 5, 9, 13, 17) // 손바닥 중심을 계산하기 위한 랜드마크 인덱스
        return palmLandmarks.map { landmarks[it].x() }.average().toFloat()
    }

    // 손가락 방향 계산 (새끼손가락에서 엄지까지의 전반적인 방향)
    private fun calculateHandDirection(landmarks: List<NormalizedLandmark>): Float {
        val fingerTips = listOf(4, 8, 12, 16, 20) // 엄지부터 새끼손가락까지의 끝점
        return fingerTips.map { landmarks[it].x() }.average().toFloat()
    }

    private class AdjustedLandmark(
        private val x: Float,
        private val y: Float,
        private val z: Float
    ) : NormalizedLandmark() {
        override fun x() = x
        override fun y() = y
        override fun z() = z
        override fun visibility(): Optional<Float> = Optional.of(1.0f)
        override fun presence(): Optional<Float> = Optional.of(1.0f)
    }

    private class SwappedHandLandmarkerResult(
        private val originalResult: HandLandmarkerResult,
        private val landmarks: List<List<NormalizedLandmark>>,
        private val worldLandmarks: List<List<com.google.mediapipe.tasks.components.containers.Landmark>>,
        private val handednesses: List<List<com.google.mediapipe.tasks.components.containers.Category>>
    ) : HandLandmarkerResult() {
        override fun timestampMs(): Long = originalResult.timestampMs()
        override fun landmarks() = landmarks
        override fun worldLandmarks() = worldLandmarks
        override fun handednesses() = handednesses
        override fun handedness(): MutableList<MutableList<Category>> {
            return handednesses.map { it.toMutableList() }.toMutableList()
        }
    }

    fun processImage(imageProxy: ImageProxy): HandLandmarkerResult? {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastProcessedTimestamp < MINIMUM_TIME_BETWEEN_PROCESSING) {
            imageProxy.close()
            return null
        }

        val buffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        return try {
            val bitmap = Bitmap.createBitmap(
                imageProxy.width,
                imageProxy.height,
                Bitmap.Config.ARGB_8888
            ).apply {
                copyPixelsFromBuffer(ByteBuffer.wrap(bytes))
            }

            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            }

            val scaledWidth = imageProxy.width
            val scaledHeight = imageProxy.height
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)

            val rotatedBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledWidth,
                scaledHeight,
                matrix,
                false
            )

            val mpImage = BitmapImageBuilder(rotatedBitmap).build()
            val rawResult = handLandmarker?.detect(mpImage, imageProcessingOptions)
            val correctedResult = rawResult?.let { fixHandedness(it) }

            lastProcessedTimestamp = currentTimestamp

            bitmap.recycle()
            if (rotatedBitmap != bitmap) {
                rotatedBitmap.recycle()
            }

            correctedResult
        } catch (e: Exception) {
            Log.e(TAG, "Error processing image", e)
            null
        }
    }

    companion object {
        private const val TAG = "HandTrackingProcessor"
    }
}