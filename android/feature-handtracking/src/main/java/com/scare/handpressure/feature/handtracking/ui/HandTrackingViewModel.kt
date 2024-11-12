package com.scare.handpressure.feature.handtracking.ui

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.scare.handpressure.feature.handtracking.domain.HandTrackingProcessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HandTrackingViewModel @Inject constructor(
    private val handTrackingProcessor: HandTrackingProcessor
) : ViewModel() {

    private val _handLandmarks = MutableStateFlow<HandLandmarkerResult?>(null)
    val handLandmarks: StateFlow<HandLandmarkerResult?> = _handLandmarks.asStateFlow()

    fun processFrame(imageProxy: ImageProxy) {
        viewModelScope.launch {
            try {
                val result = handTrackingProcessor.processImage(imageProxy)
                _handLandmarks.value = result
            } finally {
                imageProxy.close()
            }
        }
    }
}