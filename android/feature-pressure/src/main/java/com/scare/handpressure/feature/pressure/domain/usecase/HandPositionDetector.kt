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

    private fun checkWebSpacePosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        val hand1 = result.landmarks()[0]
        val hand2 = result.landmarks()[1]

        // 양손의 엄지와 검지 사이의 각도 확인
        val hand1WebSpace =
            calculateDistance(hand1[4].x(), hand1[4].y(), hand1[8].x(), hand1[8].y())
        val hand2WebSpace =
            calculateDistance(hand2[4].x(), hand2[4].y(), hand2[8].x(), hand2[8].y())

        val isCorrectPosition =
            hand1WebSpace > POSITION_THRESHOLD && hand2WebSpace > POSITION_THRESHOLD

        // 양손 모두 손바닥이 위를 향하고 있는지 확인
        val isPalmUp = hand1[9].z() < hand1[0].z() && hand2[9].z() < hand2[0].z()

        return HandPosition(
            isCorrect = isCorrectPosition && isPalmUp,
            accuracy = 1f - ((hand1WebSpace + hand2WebSpace) / (POSITION_THRESHOLD * 4)),
            feedback = when {
                !isPalmUp -> "두 손바닥 모두 위를 향하도록 해주세요"
                !isCorrectPosition -> "양손의 엄지와 검지를 더 벌려주세요"
                else -> "자세가 정확합니다"
            },
            landmarks = result.landmarks()
        )
    }

    private fun checkFingerBasePosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        val hand1 = result.landmarks()[0]
        val hand2 = result.landmarks()[1]

        // 양손의 각 손가락 기부 위치 확인
        val fingerBases1 = listOf(hand1[1], hand1[5], hand1[9], hand1[13], hand1[17])
        val fingerBases2 = listOf(hand2[1], hand2[5], hand2[9], hand2[13], hand2[17])

        // 양손 모두 손바닥이 위를 향하고 있는지 확인
        val isPalmUp = hand1[9].z() < hand1[0].z() && hand2[9].z() < hand2[0].z()

        // 각 손의 손가락 기부가 적절한 높이에 있는지 확인
        val areBasesAligned1 = fingerBases1.zipWithNext().all { (current, next) ->
            abs(current.y() - next.y()) < POSITION_THRESHOLD
        }
        val areBasesAligned2 = fingerBases2.zipWithNext().all { (current, next) ->
            abs(current.y() - next.y()) < POSITION_THRESHOLD
        }

        return HandPosition(
            isCorrect = isPalmUp && areBasesAligned1 && areBasesAligned2,
            accuracy = if (isPalmUp && areBasesAligned1 && areBasesAligned2) 1f else 0.5f,
            feedback = when {
                !isPalmUp -> "두 손바닥 모두 위를 향하도록 해주세요"
                !areBasesAligned1 || !areBasesAligned2 -> "양손의 손가락을 편하게 펴주세요"
                else -> "자세가 정확합니다"
            },
            landmarks = result.landmarks()
        )
    }

    private fun checkPinkiePosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
        val hand1 = result.landmarks()[0]
        val hand2 = result.landmarks()[1]

        // 양손의 새끼손가락 관절 위치 확인
        val isPinkieBent1 = checkPinkieBent(hand1)
        val isPinkieBent2 = checkPinkieBent(hand2)

        // 양손 모두 손바닥이 위를 향하고 있는지 확인
        val isPalmUp = hand1[9].z() < hand1[0].z() && hand2[9].z() < hand2[0].z()

        return HandPosition(
            isCorrect = isPalmUp && isPinkieBent1 && isPinkieBent2,
            accuracy = if (isPalmUp && isPinkieBent1 && isPinkieBent2) 1f else 0.5f,
            feedback = when {
                !isPalmUp -> "두 손바닥 모두 위를 향하도록 해주세요"
                !isPinkieBent1 || !isPinkieBent2 -> "양손의 새끼손가락을 살짝 구부려주세요"
                else -> "자세가 정확합니다"
            },
            landmarks = result.landmarks()
        )
    }

    private fun checkPinkieBent(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): Boolean {
        val pinkieBase = landmarks[17]
        val pinkieMid = landmarks[18]
        val pinkieTip = landmarks[20]
        return pinkieTip.y() > pinkieMid.y() && pinkieMid.y() > pinkieBase.y()
    }

    private fun checkPalmPosition(
        result: HandLandmarkerResult,
        step: PressureStep
    ): HandPosition {
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