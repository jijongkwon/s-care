package com.scare.handpressure.feature.handtracking.util

import com.google.mediapipe.tasks.vision.core.ImageProcessingOptions

object ImageProcessingUtils {

    fun createImageProcessingOptions(): ImageProcessingOptions {
        return ImageProcessingOptions.builder()
            .setRotationDegrees(0)
            .build()
    }
}