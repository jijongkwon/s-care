package com.scare.handpressure.feature.pressure.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.scare.handpressure.feature.handtracking.ui.components.CameraPreview
import com.scare.handpressure.feature.pressure.domain.model.StepState
import com.scare.handpressure.feature.pressure.ui.components.PressureTimer
import com.scare.handpressure.feature.pressure.ui.components.StepGuide
import com.scare.handpressure.feature.pressure.ui.components.StepProgressIndicator

@Composable
fun HandPressureScreen(
    modifier: Modifier = Modifier,
    viewModel: HandPressureViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val stepState by viewModel.stepState.collectAsState()
    val handPosition by viewModel.handPosition.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startSession()
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 카메라 프리뷰
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onFrameAvailable = viewModel::processImageProxy
        )

        // 손 랜드마크 오버레이
//        HandLandmarksOverlay(
//            result = handPosition?.landmarks?.firstOrNull(),
//            modifier = Modifier.fillMaxSize()
//        )

        // 상단 진행 상태
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .background(Color.White.copy(alpha = 0.8f)),

            ) {
            StepProgressIndicator(
                currentStepId = currentStep.id
            )
        }

        // 하단 컨트롤
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            if (stepState == StepState.HOLDING_POSITION) {
                PressureTimer(
                    remainingTime = remainingTime,
                    totalTime = currentStep.duration
                )
            }

            // 피드백 메시지
            handPosition?.feedback?.let { feedback ->
                Text(
                    text = feedback,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.8f)),
                    textAlign = TextAlign.Center
                )
            }

            StepGuide(
                step = currentStep,
                state = stepState,
                remainingTime = remainingTime
            )


            // 컨트롤 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.retryCurrentStep() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("다시 시도")
                }

                Button(
                    onClick = { viewModel.skipCurrentStep() }
                ) {
                    Text("다음 단계")
                }
            }
        }
    }

    // 완료 처리
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            onComplete()
        }
    }
}