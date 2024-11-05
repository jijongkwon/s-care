package com.scare.ui.mobile.calender.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scare.ui.theme.NeonYellow
import com.scare.ui.theme.Typography

@Composable
fun WeeklyReport(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f) // 버튼 너비를 80%로 설정 (조정 가능)
            .height(100.dp) // 버튼 높이
            .drawBehind {
                // 상단, 좌측, 우측만 테두리를 그립니다 (하단 제외)
                drawRoundRect(
                    color = NeonYellow,
                    topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                    size = size.copy(height = size.height + 100.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                    style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
                )
            },
        contentAlignment = Alignment.Center // 텍스트 중앙 정렬
    ) {
        Text(
            text = "주간 리포트 보러가기",
            style = Typography.titleLarge.copy( // TextStyle 적용
                fontWeight = FontWeight.Bold // 굵기 변경
            )
        )
    }
}