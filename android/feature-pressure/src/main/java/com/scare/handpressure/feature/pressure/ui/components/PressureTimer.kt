package com.scare.handpressure.feature.pressure.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PressureTimer(
    remainingTime: Int,
    totalTime: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        val progress = remainingTime.toFloat() / totalTime

        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(80.dp),
            color = when {
                progress > 0.5f -> Color.Green // 진행률이 50% 이상일 때
                progress > 0.2f -> Color.Yellow // 진행률이 20~50%일 때
                else -> Color.Red // 진행률이 20% 이하일 때
            },
            strokeWidth = 6.dp // 테두리 두께 조정
        )
        Text(text = "$remainingTime", color = Color.White, fontSize = 24.sp,)
    }
}