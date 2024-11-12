package com.scare.handpressure.feature.handtracking.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.scare.handpressure.feature.handtracking.util.ImageProcessingUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.ByteBuffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandTrackingProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var handLandmarker: HandLandmarker? = null
    private var lastProcessedTimestamp = 0L
    private val MINIMUM_TIME_BETWEEN_PROCESSING = 50L // 20fps 제한

    init {
        setupHandLandmarker()
    }

    private fun setupHandLandmarker() {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("models/hand_landmarker.task")
                .setDelegate(Delegate.GPU)  // GPU 사용
                .build()

            val options = HandLandmarker.HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinHandDetectionConfidence(0.2f)  // 적절한 감도로 조정
                .setMinHandPresenceConfidence(0.2f)
                .setMinTrackingConfidence(0.2f)
                .setRunningMode(RunningMode.IMAGE)
                .setNumHands(1)
                .build()

            handLandmarker = HandLandmarker.createFromOptions(context, options)
        } catch (e: Exception) {
            Log.e("HandTrackingProcessor", "Error setting up HandLandmarker", e)
        }
    }

    fun processImage(imageProxy: ImageProxy): HandLandmarkerResult? {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastProcessedTimestamp < MINIMUM_TIME_BETWEEN_PROCESSING) {
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
                // 미러링 처리 (전면 카메라용)
                postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )

            val mpImage = BitmapImageBuilder(rotatedBitmap).build()
            val result =
                handLandmarker?.detect(mpImage, ImageProcessingUtils.createImageProcessingOptions())
            lastProcessedTimestamp = currentTimestamp

            // 비트맵 리소스 해제
            bitmap.recycle()
            if (rotatedBitmap != bitmap) {
                rotatedBitmap.recycle()
            }

            result
        } catch (e: Exception) {
            Log.e("HandTrackingProcessor", "Error processing image", e)
            null
        }
    }
}