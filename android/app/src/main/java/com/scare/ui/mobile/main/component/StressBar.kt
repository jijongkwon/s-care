package com.scare.ui.mobile.main.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.scare.ui.theme.Typography
import com.scare.ui.theme.White
import com.scare.ui.theme.high
import com.scare.ui.theme.low
import com.scare.ui.theme.medium

@Composable
fun MyStressRate(stress: Double,
                 modifier: Modifier = Modifier
) {
    val stressLevel = when {
        stress <= 60 -> "낮음"
        stress <= 90 -> "보통"
        else -> "높음"
    }

    val rate = stress.toFloat() / 200F

    Column (
        modifier.padding(horizontal = 16.dp)
    ) {
        StressBar(rate)

        Row (
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val textColor = when (stressLevel) {
                "낮음" -> low
                "보통" -> medium
                else -> high
            }

            Text(
                text="스트레스 $stressLevel",
                style = Typography.bodyLarge.copy( // TextStyle 적용
                    color = textColor, // 색깔 변경
                    fontWeight = FontWeight.Medium // 굵기 변경
                ))
            Text(
                text="$stress",
                style = Typography.labelSmall.copy( // TextStyle 적용
                    fontSize = 24.sp, // 크기 변경
                    color = textColor, // 색깔 변경
                    fontWeight = FontWeight.Bold // 굵기 변경
                ))
        }
    }
}

@Composable
fun StressBar(rate: Float, modifier: Modifier = Modifier.padding(vertical = 16.dp)) {
    val gradient = Brush.linearGradient(
        colors = listOf(low, medium, high), // Gradient 색상 설정
        start = Offset.Zero,
        end = Offset(1000f, 0f) // 가로 방향 Gradient
    )

    // Box의 너비를 저장하기 위한 상태
    var barWidth = remember { mutableStateOf(0f) }

    val stressPoint = when {
        rate <= 0.3f -> low
        rate <= 0.6f -> medium
        else -> high
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(brush = gradient, shape = RoundedCornerShape(10.dp)) // 둥근 모서리 배경
            .onSizeChanged { size ->
                barWidth.value = size.width.toFloat() // Box의 실제 너비를 상태에 저장
            }
    ) {
        if (barWidth.value > 0) {
            val circleOffset = rate * barWidth.value

            Canvas(
                modifier = Modifier
                    .size(28.dp) // 원을 막대보다 크게 설정
                    .align(Alignment.CenterStart)
                    .offset(
                        x = with(LocalDensity.current) { circleOffset.toDp() },
                    )
                    .zIndex(10f)
            ) {
                drawCircle(
                    color = White,
                    radius = size.minDimension
                )

                drawCircle(
                    color = stressPoint,
                    radius = size.minDimension - 4.dp.toPx()
                )
            }
        }
    }
}