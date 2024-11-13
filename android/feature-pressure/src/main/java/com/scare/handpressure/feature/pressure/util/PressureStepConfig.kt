package com.scare.handpressure.feature.pressure.util

import com.scare.handpressure.feature.pressure.domain.model.PressureStep

object PressureStepConfig {
    val steps = listOf(
        PressureStep(
            id = 1,
            title = "유니온 밸리 포인트",
            description = "엄지와 검지 사이의 웹 부분을 자극합니다",
            duration = 5,
            targetLandmarks = listOf(1, 2, 4),
            instructions = listOf(
                "손바닥이 서로 마주보도록 위치시킵니다",
                "엄지와 검지 사이의 살이 많은 부분을 찾습니다",
                "적당한 강도로 4-5초간 눌러줍니다",
                "깊게 호흡하며 긴장을 풀어줍니다"
            ),
            requiredHandCount = 2
        ),

        PressureStep(
            id = 2,
            title = "손바닥 웹 공간 마사지",
            description = "엄지와 검지 사이 부위를 마사지합니다",
            duration = 15,
            targetLandmarks = listOf(1, 2, 4),
            instructions = listOf(
                "왼손을 들어 손바닥이 보이도록 합니다",
                "손가락을 모아줍니다",
                "오른손 엄지로 웹 공간을 마사지합니다",
                "천천히 15까지 세어줍니다",
                "반대 손도 같은 방법으로 진행합니다"
            )
        ),
        
        PressureStep(
            id = 3,
            title = "손가락 베이스 서클",
            description = "각 손가락 밑부분을 원을 그리며 마사지합니다",
            duration = 25,
            targetLandmarks = listOf(1, 5, 9, 13, 17),
            instructions = listOf(
                "손바닥이 위를 향하도록 합니다",
                "반대쪽 엄지로 작은 원을 그리며 마사지합니다",
                "각 손가락당 5초씩 진행합니다",
                "3-5회 반복합니다",
                "부드럽게 압력을 가합니다"
            )
        ),

        PressureStep(
            id = 4,
            title = "새끼손가락 압점",
            description = "새끼손가락 첫마디 아래를 마사지합니다",
            duration = 20,
            targetLandmarks = listOf(17, 18),
            instructions = listOf(
                "새끼손가락 첫마디 아래 부분을 찾습니다",
                "엄지와 검지로 부드럽게 잡습니다",
                "시계 반대 방향으로 작은 원을 그립니다",
                "20초간 유지합니다",
                "각 손당 최대 5회 반복합니다"
            )
        ),

        PressureStep(
            id = 5,
            title = "손바닥과 손가락 통합 마사지",
            description = "손바닥 마사지와 손가락 스트레칭을 결합합니다",
            duration = 30,
            targetLandmarks = listOf(0, 1, 5, 9, 13, 17),
            instructions = listOf(
                "손가락을 깍지 끼고 손바닥을 원을 그리며 문지릅니다",
                "반대쪽 엄지 아래를 마사지합니다",
                "손바닥 중심으로 이동하며 마사지합니다",
                "손목을 돌리고 전체적으로 주무릅니다",
                "각 손가락을 부드럽게 당깁니다",
                "손가락 사이를 부드럽게 꼬집듯이 마사지합니다"
            )
        )
    )
}