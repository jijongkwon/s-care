package com.scare.handpressure.feature.handtracking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.scare.handpressure.feature.handtracking.ui.components.CameraPreview
import com.scare.handpressure.feature.handtracking.ui.components.HandLandmarksOverlay

@Composable
fun HandTrackingScreen(
    viewModel: HandTrackingViewModel = hiltViewModel()
) {
    val handLandmarkerResult by viewModel.handLandmarks.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onFrameAvailable = { image ->
                viewModel.processFrame(image)
            }
        )

        HandLandmarksOverlay(
            result = handLandmarkerResult,
            modifier = Modifier.fillMaxSize()
        )
    }
}