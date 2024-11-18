package com.scare.handpressure.feature.handtracking.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

@Composable
fun HandLandmarksOverlay(
    landmarks: List<List<NormalizedLandmark>>?, // 손 랜드마크 데이터
    currentStep: Int, // 현재 단계 (1: 왼손, 2: 오른손)
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2  // Canvas의 가로 중심
            val centerY = size.height / 2 // Canvas의 세로 중심

            val scaleFactorX = 2f
            val scaleFactorY = 1f

            // 현재 단계에 따라 렌더링할 손 선택
            val handToRender = when (currentStep) {
                1 -> listOf(1) // 왼손만
                2 -> listOf(0) // 오른손만
                else -> listOf(0, 1) // 기본적으로 두 손 모두
            }

            landmarks?.forEachIndexed { handIndex, handLandmarks ->
                if (handIndex in handToRender) { // 현재 단계에 해당하는 손만 렌더링
                    val handColor = when (handIndex) {
                        0 -> Color(0xFF00FF00) // 왼손: 밝은 초록색
                        1 -> Color(0xFF00BFFF) // 오른손: 밝은 파란색
                        else -> Color.Yellow
                    }

                    // 왼손/오른손 구분 오프셋
                    val offsetX = if (handIndex == 0) -50f else 50f
                    val offsetY = -30f

                    handLandmarks.forEach { landmark ->
                        val scaledX =
                            centerX + (landmark.x() * size.width - centerX) * scaleFactorX + offsetX
                        val scaledY =
                            centerY + (landmark.y() * size.height - centerY) * scaleFactorY + offsetY

                        // 랜드마크 포인트 그리기
                        drawCircle(
                            color = handColor,
                            radius = 12f,
                            center = androidx.compose.ui.geometry.Offset(
                                scaledX,
                                scaledY
                            ),
                            style = Stroke(width = 3f)
                        )
                    }

                    // 랜드마크 연결선 그리기
                    val connections = getHandConnections()
                    connections.forEach { (start, end) ->
                        if (start < handLandmarks.size && end < handLandmarks.size) {
                            // 시작 점
                            val startX =
                                centerX + (handLandmarks[start].x() * size.width - centerX) * scaleFactorX + offsetX
                            val startY =
                                centerY + (handLandmarks[start].y() * size.height - centerY) * scaleFactorY + offsetY

                            // 끝 점
                            val endX =
                                centerX + (handLandmarks[end].x() * size.width - centerX) * scaleFactorX + offsetX
                            val endY =
                                centerY + (handLandmarks[end].y() * size.height - centerY) * scaleFactorY + offsetY

                            drawLine(
                                color = handColor.copy(alpha = 0.5f),
                                start = androidx.compose.ui.geometry.Offset(startX, startY),
                                end = androidx.compose.ui.geometry.Offset(endX, endY),
                                strokeWidth = 2f
                            )
                        }
                    }
                }
            }
        }
    }
}



// 손가락 관절을 연결하는 선을 위한 인덱스 쌍 정의
private fun getHandConnections(): List<Pair<Int, Int>> {
    return listOf(
        // 엄지손가락
        Pair(0, 1), Pair(1, 2), Pair(2, 3), Pair(3, 4),
        // 검지손가락
        Pair(0, 5), Pair(5, 6), Pair(6, 7), Pair(7, 8),
        // 중지손가락
        Pair(0, 9), Pair(9, 10), Pair(10, 11), Pair(11, 12),
        // 약지손가락
        Pair(0, 13), Pair(13, 14), Pair(14, 15), Pair(15, 16),
        // 새끼손가락
        Pair(0, 17), Pair(17, 18), Pair(18, 19), Pair(19, 20),
        // 손바닥
        Pair(5, 9), Pair(9, 13), Pair(13, 17)
    )
}