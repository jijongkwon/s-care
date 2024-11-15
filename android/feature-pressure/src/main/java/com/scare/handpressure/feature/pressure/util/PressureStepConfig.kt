package com.scare.handpressure.feature.pressure.util

import com.scare.handpressure.feature.pressure.domain.model.PressureStep

object PressureStepConfig {
    val steps = listOf(
        PressureStep(
            id = 1,
            title = "유니온 밸리 포인트 (왼손)",
            description = "엄지와 검지 사이의 웹 부분을 자극합니다",
            duration = 30,
            targetLandmarks = listOf(1, 2, 4),
            instructions = listOf(
                "손바닥이 서로 마주보도록 위치시킵니다",
                "엄지와 검지 사이의 살이 많은 부분을 찾습니다",
                "적당한 강도로 30초간 눌러줍니다",
                "깊게 호흡하며 긴장을 풀어줍니다"
            ),
            requiredHandCount = 2
        ),

        PressureStep(
            id = 2,
            title = "유니온 밸리 포인트 (오른손)",
            description = "엄지와 검지 사이의 웹 부분을 자극합니다",
            duration = 30,  // 시간만 30초로 변경
            targetLandmarks = listOf(1, 2, 4),
            instructions = listOf(
                "손바닥이 서로 마주보도록 위치시킵니다",
                "엄지와 검지 사이의 살이 많은 부분을 찾습니다",
                "적당한 강도로 30초간 눌러줍니다",
                "깊게 호흡하며 긴장을 풀어줍니다"
            ),
            requiredHandCount = 2
        ),
    )
}