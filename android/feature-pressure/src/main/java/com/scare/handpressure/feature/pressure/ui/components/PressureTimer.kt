package com.scare.handpressure.feature.pressure.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PressureTimer(
    remainingTime: Int,
    totalTime: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { remainingTime.toFloat() / totalTime },
            modifier = Modifier.size(80.dp),
        )
        Text(text = "$remainingTime")
    }
}