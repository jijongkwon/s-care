package com.scare.handpressure.feature.pressure.domain.model

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

data class HandPosition(
    val isCorrect: Boolean,
    val accuracy: Float,
    val feedback: String,
    val landmarks: List<List<NormalizedLandmark>>? = null
)