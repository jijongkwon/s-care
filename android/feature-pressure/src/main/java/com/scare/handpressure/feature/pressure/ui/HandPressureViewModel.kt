package com.scare.handpressure.feature.pressure.ui

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.scare.handpressure.feature.handtracking.domain.HandTrackingProcessor
import com.scare.handpressure.feature.pressure.domain.model.HandPosition
import com.scare.handpressure.feature.pressure.domain.model.PressureStep
import com.scare.handpressure.feature.pressure.domain.model.StepState
import com.scare.handpressure.feature.pressure.domain.usecase.HandPositionDetector
import com.scare.handpressure.feature.pressure.util.PressureStepConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HandPressureViewModel @Inject constructor(
    private val handPositionDetector: HandPositionDetector,
    private val handTrackingProcessor: HandTrackingProcessor
) : ViewModel() {

    private val _currentStep = MutableStateFlow<PressureStep>(PressureStepConfig.steps.first())
    val currentStep: StateFlow<PressureStep> = _currentStep.asStateFlow()

    private val _stepState = MutableStateFlow<StepState>(StepState.NOT_STARTED)
    val stepState: StateFlow<StepState> = _stepState.asStateFlow()

    private val _handPosition = MutableStateFlow<HandPosition?>(null)
    val handPosition: StateFlow<HandPosition?> = _handPosition.asStateFlow()

    private val _remainingTime = MutableStateFlow<Int>(0)
    val remainingTime: StateFlow<Int> = _remainingTime.asStateFlow()

    private var timerJob: Job? = null
    private var lastPausedTime: Int? = null  // 마지막으로 일시정지된 시간을 저장

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _progress = MutableStateFlow(0f)

    fun startSession() {
        _stepState.value = StepState.DETECTING_POSITION
        _currentStep.value = PressureStepConfig.steps.first()
        _remainingTime.value = _currentStep.value.duration
        _progress.value = 0f
        _isCompleted.value = false
        lastPausedTime = null
    }

    private fun processFrame(result: HandLandmarkerResult?) {
        viewModelScope.launch {
            val position = handPositionDetector.checkPosition(result, _currentStep.value)
            _handPosition.value = position

            when (_stepState.value) {
                StepState.DETECTING_POSITION, StepState.POSITION_INCORRECT -> {
                    if (position.isCorrect) {
                        _stepState.value = StepState.HOLDING_POSITION
                        // 이전에 중단된 시간이 있으면 그 시간부터, 없으면 처음부터 시작
                        startTimer(lastPausedTime ?: _currentStep.value.duration)
                    }
                }

                StepState.HOLDING_POSITION -> {
                    if (!position.isCorrect) {
                        _stepState.value = StepState.POSITION_INCORRECT
                        pauseTimer()
                    }
                }

                else -> { /* 다른 상태에서는 처리하지 않음 */
                }
            }
        }
    }

    private fun startTimer(startFromSeconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val totalDuration = _currentStep.value.duration
            _remainingTime.value = startFromSeconds

            for (i in startFromSeconds downTo 0) {
                _remainingTime.value = i
                _progress.value = 1f - (i.toFloat() / totalDuration)
                delay(1000) // 1초 딜레이

                if (i == 0) {
                    completeCurrentStep()
                }
            }
        }
    }

    private fun pauseTimer() {
        lastPausedTime = _remainingTime.value  // 현재 남은 시간을 저장
        timerJob?.cancel()
    }

    private fun stopTimer() {
        timerJob?.cancel()
        lastPausedTime = null
        _progress.value = 0f
    }

    private fun completeCurrentStep() {
        val currentStepIndex = PressureStepConfig.steps.indexOf(_currentStep.value)

        viewModelScope.launch {
            if (currentStepIndex < PressureStepConfig.steps.size - 1) {
                _stepState.value = StepState.COMPLETED
                delay(2000)

                _currentStep.value = PressureStepConfig.steps[currentStepIndex + 1]
                _stepState.value = StepState.DETECTING_POSITION
                _remainingTime.value = _currentStep.value.duration
                _progress.value = 0f
                lastPausedTime = null  // 새로운 스텝을 시작할 때 리셋
            } else {
                _stepState.value = StepState.COMPLETED_ALL
                delay(3000)
                _isCompleted.value = true
            }
        }
    }

    fun skipCurrentStep() {
        stopTimer()
        viewModelScope.launch {
            _stepState.value = StepState.SKIPPED
            delay(1500)
            proceedToNextStep()
        }
    }

    private fun proceedToNextStep() {
        val currentStepIndex = PressureStepConfig.steps.indexOf(_currentStep.value)

        if (currentStepIndex < PressureStepConfig.steps.size - 1) {
            _currentStep.value = PressureStepConfig.steps[currentStepIndex + 1]
            _stepState.value = StepState.DETECTING_POSITION
            _remainingTime.value = _currentStep.value.duration
            _progress.value = 0f
            lastPausedTime = null  // 새로운 스텝으로 넘어갈 때 리셋
        } else {
            viewModelScope.launch {
                _stepState.value = StepState.COMPLETED_ALL
                delay(3000)
                _isCompleted.value = true
            }
        }
    }

    fun retryCurrentStep() {
        stopTimer()
        _stepState.value = StepState.DETECTING_POSITION
        _remainingTime.value = _currentStep.value.duration
        _progress.value = 0f
        lastPausedTime = null  // 재시도할 때 리셋
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }

    fun processImageProxy(imageProxy: ImageProxy) {
        viewModelScope.launch {
            val result = handTrackingProcessor.processImage(imageProxy)
            processFrame(result)
            imageProxy.close()
        }
    }
}