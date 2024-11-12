package com.scare.handpressure.feature.handtracking.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult

@Composable
fun HandLandmarksOverlay(
    result: HandLandmarkerResult?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 함수 내부에서 선언된 가로와 세로 간격 스케일 변수
            val scaleFactorX = 2f  // 가로 축 간격 조정
            val scaleFactorY = 1f  // 세로 축 간격 조정

            result?.landmarks()?.firstOrNull()?.forEachIndexed { index, landmark ->
                val centerX = size.width / 2
                val centerY = size.height / 2

                // 위치 조정을 위한 오프셋
                val offsetX = 0f  // 더 왼쪽으로 이동
                val offsetY = -30f    // 약간 아래로 이동

                // x와 y 좌표의 스케일 조정
                val scaledX =
                    centerX + (landmark.x() * size.width - centerX) * scaleFactorX + offsetX
                val scaledY =
                    centerY + (landmark.y() * size.height - centerY) * scaleFactorY + offsetY

                drawCircle(
                    color = Color.Green,
                    radius = 12f,  // 점의 크기도 약간 줄임
                    center = androidx.compose.ui.geometry.Offset(
                        scaledX,
                        scaledY
                    ),
                    style = Stroke(width = 3f)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(
                text = "Landmarks detected: ${result?.landmarks()?.firstOrNull()?.size ?: 0}",
                color = Color.Green
            )
        }
    }
}
