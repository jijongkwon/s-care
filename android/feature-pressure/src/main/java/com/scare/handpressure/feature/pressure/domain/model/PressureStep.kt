package com.scare.handpressure.feature.pressure.domain.model

data class PressureStep(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int,
    val targetLandmarks: List<Int>,
    val instructions: List<String>,
    val requiredHandCount: Int = 1
)
