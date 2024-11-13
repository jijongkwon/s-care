package com.scare.handpressure.feature.pressure.domain.usecase

import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.scare.handpressure.feature.pressure.domain.model.HandPosition
import com.scare.handpressure.feature.pressure.domain.model.PressureStep
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sqrt

class HandPositionDetector @Inject constructor() {

    companion object {
        private const val TAG = "HandPositionDetector"
        private const val POSITION_THRESHOLD = 0.15f
        private const val DISTANCE_THRESHOLD = 0.1f
        private const val ANGLE_THRESHOLD = 30f
        private const val CIRCLE_MOVEMENT_THRESHOLD = 0.05f
    }

    fun checkPosition(
        result: HandLandmarkerResult?,
        currentStep: PressureStep
    ): HandPosition {
        if (result == null || result.landmarks().isEmpty()) {
            return HandPosition(
                isCorrect = false,
                accuracy = 0f,
                feedback = "손이 인식되지 않습니다",
                landmarks = null
            )
        }

        return when (currentStep.id) {
            1 -> checkUnionValleyPosition(result, currentStep)
            2 -> checkWebSpacePosition(result, currentStep)
            3 -> checkFingerBasePosition(result, currentStep)
            4 -> checkPinkiePosition(result, currentStep)
            5 -> checkPalmPosition(result, currentStep)
            else -> HandPosition(false, 0f, "알 수 없는 단계입니다", result.landmarks())
        }
    }

    private fun checkUnionValleyPosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        if (result.landmarks().size < 2) {
            return HandPosition(
                isCorrect = false,
                accuracy = 0f,
                feedback = "두 손이 모두 필요합니다",
                landmarks = result.landmarks()
            )
        }

        val hand1 = result.landmarks()[0]
        val hand2 = result.landmarks()[1]

        // 두 손의 웹 공간(엄지-검지 사이) 거리 확인
        val distance = calculateDistance(
            hand1[1].x(), hand1[1].y(),  // 첫 번째 손의 엄지
            hand2[4].x(), hand2[4].y()   // 두 번째 손의 검지
        )

        val isCorrect = distance < DISTANCE_THRESHOLD
        val accuracy = 1f - (distance / DISTANCE_THRESHOLD)

        return HandPosition(
            isCorrect = isCorrect,
            accuracy = accuracy,
            feedback = if (isCorrect) "자세가 정확합니다" else "두 손의 웹 공간을 더 가깝게 해주세요",
            landmarks = result.landmarks()
        )
    }

    private fun checkWebSpacePosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        val landmarks = result.landmarks().firstOrNull() ?: return HandPosition(
            isCorrect = false,
            accuracy = 0f,
            feedback = "손이 인식되지 않습니다",
            landmarks = result.landmarks()
        )

        // 엄지와 검지 사이의 각도 확인
        val thumbTipX = landmarks[4].x()
        val thumbTipY = landmarks[4].y()
        val indexTipX = landmarks[8].x()
        val indexTipY = landmarks[8].y()

        val webSpaceDistance = calculateDistance(thumbTipX, thumbTipY, indexTipX, indexTipY)
        val isCorrectPosition = webSpaceDistance > POSITION_THRESHOLD

        // 손바닥이 위를 향하고 있는지 확인
        val isPalmUp = landmarks[9].z() < landmarks[0].z()

        return HandPosition(
            isCorrect = isCorrectPosition && isPalmUp,
            accuracy = 1f - (webSpaceDistance / (POSITION_THRESHOLD * 2)),
            feedback = when {
                !isPalmUp -> "손바닥이 위를 향하도록 해주세요"
                !isCorrectPosition -> "엄지와 검지를 더 벌려주세요"
                else -> "자세가 정확합니다"
            },
            landmarks = result.landmarks()
        )
    }

    private fun checkFingerBasePosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        val landmarks = result.landmarks().firstOrNull() ?: return HandPosition(
            isCorrect = false,
            accuracy = 0f,
            feedback = "손이 인식되지 않습니다",
            landmarks = result.landmarks()
        )

        // 각 손가락 기부의 위치 확인
        val fingerBases = listOf(
            landmarks[1],  // 엄지
            landmarks[5],  // 검지
            landmarks[9],  // 중지
            landmarks[13], // 약지
            landmarks[17]  // 소지
        )

        // 손바닥이 위를 향하고 있는지 확인
        val isPalmUp = landmarks[9].z() < landmarks[0].z()

        // 각 손가락 기부가 적절한 높이에 있는지 확인
        val areBasesAligned = fingerBases.zipWithNext().all { (current, next) ->
            abs(current.y() - next.y()) < POSITION_THRESHOLD
        }

        return HandPosition(
            isCorrect = isPalmUp && areBasesAligned,
            accuracy = if (isPalmUp && areBasesAligned) 1f else 0.5f,
            feedback = when {
                !isPalmUp -> "손바닥이 위를 향하도록 해주세요"
                !areBasesAligned -> "손가락을 편하게 펴주세요"
                else -> "자세가 정확합니다"
            },
            landmarks = result.landmarks()
        )
    }

    private fun checkPinkiePosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        val landmarks = result.landmarks().firstOrNull() ?: return HandPosition(
            isCorrect = false,
            accuracy = 0f,
            feedback = "손이 인식되지 않습니다",
            landmarks = result.landmarks()
        )

        // 새끼손가락 관절 위치 확인
        val pinkieBase = landmarks[17]
        val pinkieMid = landmarks[18]
        val pinkieTip = landmarks[20]

        // 새끼손가락이 구부러져 있는지 확인
        val isPinkieBent = pinkieTip.y() > pinkieMid.y() && pinkieMid.y() > pinkieBase.y()

        // 손바닥이 위를 향하고 있는지 확인
        val isPalmUp = landmarks[9].z() < landmarks[0].z()

        return HandPosition(
            isCorrect = isPalmUp && isPinkieBent,
            accuracy = if (isPalmUp && isPinkieBent) 1f else 0.5f,
            feedback = when {
                !isPalmUp -> "손바닥이 위를 향하도록 해주세요"
                !isPinkieBent -> "새끼손가락을 살짝 구부려주세요"
                else -> "자세가 정확합니다"
            },
            landmarks = result.landmarks()
        )
    }

    private fun checkPalmPosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        if (result.landmarks().size < 2) {
            return HandPosition(
                isCorrect = false,
                accuracy = 0f,
                feedback = "두 손이 모두 필요합니다",
                landmarks = result.landmarks()
            )
        }

        val hand1 = result.landmarks()[0]
        val hand2 = result.landmarks()[1]

        // 두 손바닥의 중심점이 가까이 있는지 확인
        val palm1Center = calculatePalmCenter(hand1)
        val palm2Center = calculatePalmCenter(hand2)

        val palmDistance = calculateDistance(
            palm1Center.first, palm1Center.second,
            palm2Center.first, palm2Center.second
        )

        val arePalmsClose = palmDistance < DISTANCE_THRESHOLD

        // 손가락이 교차되어 있는지 확인
        val areFingersInterlocked = checkFingersInterlocked(hand1, hand2)

        return HandPosition(
            isCorrect = arePalmsClose && areFingersInterlocked,
            accuracy = 1f - (palmDistance / (DISTANCE_THRESHOLD * 2)),
            feedback = when {
                !arePalmsClose -> "손바닥을 더 가까이 붙여주세요"
                !areFingersInterlocked -> "손가락을 교차하여 깍지를 껴주세요"
                else -> "자세가 정확합니다"
            },
            landmarks = result.landmarks()
        )
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }

    private fun calculatePalmCenter(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): Pair<Float, Float> {
        val palmLandmarks = listOf(0, 5, 9, 13, 17) // 손바닥 중심을 계산하기 위한 랜드마크 인덱스
        val centerX = palmLandmarks.map { landmarks[it].x() }.average().toFloat()
        val centerY = palmLandmarks.map { landmarks[it].y() }.average().toFloat()
        return Pair(centerX, centerY)
    }

    private fun checkFingersInterlocked(
        hand1: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>,
        hand2: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>
    ): Boolean {
        // 손가락 끝점들의 상대적 위치를 확인
        val fingerTips1 = listOf(8, 12, 16, 20) // 검지~새끼손가락 끝
        val fingerTips2 = listOf(8, 12, 16, 20)

        var interlockedCount = 0
        for (i in fingerTips1.indices) {
            val tip1Y = hand1[fingerTips1[i]].y()
            val tip2Y = hand2[fingerTips2[i]].y()

            // 손가락이 교차되어 있으면 카운트 증가
            if ((i % 2 == 0 && tip1Y < tip2Y) || (i % 2 == 1 && tip1Y > tip2Y)) {
                interlockedCount++
            }
        }

        return interlockedCount >= 3 // 최소 3개의 손가락이 교차되어 있어야 함
    }
}