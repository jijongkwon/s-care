package com.scare.handpressure.feature.pressure.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scare.handpressure.feature.pressure.domain.model.PressureStep
import com.scare.handpressure.feature.pressure.domain.model.StepState

@Composable
fun StepGuide(
    step: PressureStep,
    state: StepState,
    remainingTime: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state != StepState.COMPLETED_ALL && state != StepState.SKIPPED) {
            Text(
                text = "단계 ${step.id}: ${step.title}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = step.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        val (stateText, stateColor) = when (state) {
            StepState.NOT_STARTED -> "시작하려면 화면을 터치하세요" to MaterialTheme.colorScheme.primary
            StepState.DETECTING_POSITION -> "손 위치를 맞춰주세요" to MaterialTheme.colorScheme.primary
            StepState.POSITION_INCORRECT -> "올바른 자세로 조정해주세요" to MaterialTheme.colorScheme.error
            StepState.HOLDING_POSITION -> "유지: ${remainingTime}초" to MaterialTheme.colorScheme.primary
            StepState.COMPLETED -> "잘하셨습니다! 다음 단계로 이동합니다" to MaterialTheme.colorScheme.primary
            StepState.SKIPPED -> "단계를 건너뛰셨네요" to MaterialTheme.colorScheme.secondary
            StepState.COMPLETED_ALL -> "모든 단계를 완료했어요! 훌륭합니다" to MaterialTheme.colorScheme.primary
        }

        Text(
            text = stateText,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = when (state) {
                    StepState.COMPLETED_ALL -> 26.sp
                    StepState.SKIPPED -> 22.sp
                    else -> 20.sp
                }
            ),
            color = stateColor,
            textAlign = TextAlign.Center
        )

        if (state == StepState.COMPLETED_ALL) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "건강한 하루 보내세요 😊",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        } else if (state != StepState.COMPLETED && state != StepState.SKIPPED) {
            Spacer(modifier = Modifier.height(20.dp))

            step.instructions.forEach { instruction ->
                Text(
                    text = "• $instruction",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}