package com.scare.handpressure.feature.pressure.domain.usecase

import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.scare.handpressure.feature.pressure.domain.model.HandPosition
import com.scare.handpressure.feature.pressure.domain.model.PressureStep
import javax.inject.Inject
import kotlin.math.sqrt

class HandPositionDetector @Inject constructor() {

    companion object {
        private const val TAG = "HandPositionDetector"
        private const val DISTANCE_THRESHOLD = 0.1f
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

        // 모든 단계에서 두 손 인식 여부 확인
        if (result.landmarks().size < 2) {
            return HandPosition(
                isCorrect = false,
                accuracy = 0f,
                feedback = "두 손이 모두 필요합니다",
                landmarks = result.landmarks()
            )
        }

        return when (currentStep.id) {
            1 -> checkUnionValleyPosition(result, currentStep)
            2 -> checkUnionValleyPosition(result, currentStep)
            else -> HandPosition(false, 0f, "알 수 없는 단계입니다", result.landmarks())
        }
    }

    private fun checkUnionValleyPosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        val hand1 = result.landmarks()[0]
        val hand2 = result.landmarks()[1]

        // 첫 번째 손의 웹 스페이스 위치 계산
        val hand1WebCenter = Pair(
            (hand1[1].x() + hand1[5].x()) / 2,
            (hand1[1].y() + hand1[5].y()) / 2
        )

        // 두 번째 손의 웹 스페이스 위치 계산
        val hand2WebCenter = Pair(
            (hand2[1].x() + hand2[5].x()) / 2,
            (hand2[1].y() + hand2[5].y()) / 2
        )

        // 웹 스페이스 간의 거리 계산
        val webSpaceDistance = calculateDistance(
            hand1WebCenter.first, hand1WebCenter.second,
            hand2WebCenter.first, hand2WebCenter.second
        )

        // 첫 번째 손의 검지가 두 번째 손의 웹 스페이스 안에 있는지 확인
        val isHand1InWebSpace = isPointInWebSpace(
            hand1[8].x(), hand1[8].y(),
            hand2[1].x(), hand2[1].y(),
            hand2[5].x(), hand2[5].y()
        )

        // 두 번째 손의 검지가 첫 번째 손의 웹 스페이스 안에 있는지 확인
        val isHand2InWebSpace = isPointInWebSpace(
            hand2[8].x(), hand2[8].y(),
            hand1[1].x(), hand1[1].y(),
            hand1[5].x(), hand1[5].y()
        )

        // 거리 기준을 더 관대하게 조정
        val distanceThreshold = DISTANCE_THRESHOLD * 2f

        // 웹 스페이스 안에 있으면 거리 기준을 더 관대하게 적용
        val isCorrect = if (isHand1InWebSpace || isHand2InWebSpace) {
            webSpaceDistance < distanceThreshold
        } else {
            false
        }

        val accuracy = if (isCorrect) {
            1f - (webSpaceDistance / distanceThreshold).coerceIn(0f, 1f)
        } else {
            0f
        }

        val feedback = when {
            !isHand1InWebSpace && !isHand2InWebSpace -> "손가락을 서로의 웹 스페이스 안으로 넣어주세요"
            isCorrect -> "자세가 정확합니다"
            else -> "자세를 유지한 채로 두 손을 더 가깝게 해주세요"
        }

        return HandPosition(
            isCorrect = isCorrect,
            accuracy = accuracy,
            feedback = feedback,
            landmarks = result.landmarks()
        )
    }

    private fun isPointInWebSpace(
        px: Float, py: Float,
        x1: Float, y1: Float,
        x2: Float, y2: Float
    ): Boolean {
        val centerX = (x1 + x2) / 2
        val centerY = (y1 + y2) / 2

        // 웹 스페이스의 반경을 더 크게 설정
        val radius = calculateDistance(x1, y1, x2, y2) / 1.5f  // 반경을 더 크게

        val distanceToCenter = calculateDistance(px, py, centerX, centerY)
        return distanceToCenter <= radius * 1.3f  // 판정 범위를 더 크게
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }
}