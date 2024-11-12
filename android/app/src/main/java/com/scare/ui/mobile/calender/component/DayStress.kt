package com.scare.ui.mobile.calender.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scare.R
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.NeonYellow
import com.scare.ui.theme.Typography
import com.scare.util.getPetFace

@Composable
fun DayStress(
    modifier: Modifier,
    stress: Int
) {

    val petFace = getPetFace(stress)
    val stressText = if (stress == -1) "-" else "평균 스트레스 $stress"

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image (
            painter = petFace,
            contentDescription = "DayStressFace",
            modifier = Modifier.size(85.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box (
            modifier.clip(RoundedCornerShape(14.dp)) // 먼저 모서리를 둥글게 잘라내고
                .background(color = NeonYellow) // 배경 색상을 적용
                .height(50.dp)
                .width(180.dp),
            contentAlignment = Alignment.Center // 텍스트를 중앙에 배치
        ) {
            Text(
                text = stressText,
                style = Typography.bodyLarge.copy( // TextStyle 적용
                    color = DarkNavy, // 색깔 변경
                    fontWeight = FontWeight.Bold // 굵기 변경
                ),
            )
        }
    }
}