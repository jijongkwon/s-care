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

        if (result.landmarks().size < 2) {
            return HandPosition(
                isCorrect = false,
                accuracy = 0f,
                feedback = "두 손이 모두 필요합니다",
                landmarks = result.landmarks()
            )
        }

        return when (currentStep.id) {
            1 -> checkUnionValleyPosition(result, isLeftHand = true)
            2 -> checkUnionValleyPosition(result, isLeftHand = false)
            else -> HandPosition(false, 0f, "알 수 없는 단계입니다", result.landmarks())
        }
    }

    private fun checkUnionValleyPosition(
        result: HandLandmarkerResult,
        isLeftHand: Boolean
    ): HandPosition {
        val leftHand = result.landmarks()[0]  // 왼손
        val rightHand = result.landmarks()[1]  // 오른손

        // 각 손의 웹 스페이스 중심점 계산
        val leftWebCenter = Pair(
            (leftHand[1].x() + leftHand[5].x()) / 2,
            (leftHand[1].y() + leftHand[5].y()) / 2
        )
        val rightWebCenter = Pair(
            (rightHand[1].x() + rightHand[5].x()) / 2,
            (rightHand[1].y() + rightHand[5].y()) / 2
        )

        // 웹 스페이스 간의 거리 계산
        val webSpaceDistance = calculateDistance(
            leftWebCenter.first, leftWebCenter.second,
            rightWebCenter.first, rightWebCenter.second
        )

        // 왼손 단계인지 오른손 단계인지에 따라 다른 검사
        val isCorrectHandPosition = if (isLeftHand) {
            // 왼손 단계: 왼손 검지가 오른손 웹스페이스에 있어야 함
            isPointInWebSpace(
                leftHand[8].x(), leftHand[8].y(),
                rightHand[1].x(), rightHand[1].y(),
                rightHand[5].x(), rightHand[5].y()
            )
        } else {
            // 오른손 단계: 오른손 검지가 왼손 웹스페이스에 있어야 함
            isPointInWebSpace(
                rightHand[8].x(), rightHand[8].y(),
                leftHand[1].x(), leftHand[1].y(),
                leftHand[5].x(), leftHand[5].y()
            )
        }

        val distanceThreshold = DISTANCE_THRESHOLD * 2f
        val isCorrect = isCorrectHandPosition && webSpaceDistance < distanceThreshold

        val accuracy = if (isCorrect) {
            1f - (webSpaceDistance / distanceThreshold).coerceIn(0f, 1f)
        } else {
            0f
        }

        val feedback = when {
            !isCorrectHandPosition -> if (isLeftHand)
                "왼손 검지로 오른손 웹 스페이스를 자극해주세요"
            else
                "오른손 검지로 왼손 웹 스페이스를 자극해주세요"

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
        return distanceToCenter <= radius * 1.35f  // 판정 범위를 더 크게
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }
}