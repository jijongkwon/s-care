package com.scare.ui.mobile.calender.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scare.R
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.Gray
import com.scare.ui.theme.NeonYellow

@Composable
fun WeeklyStressResult(
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium) // 둥근 모서리 적용
            .background(NeonYellow) // 배경색 적용
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 지난주 스트레스
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "지난 주 스트레스",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkNavy,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "84",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                )
            }

            Column {
                Spacer(modifier = Modifier.size(20.dp))

                // 화살표
                Image(
                    painter = painterResource(R.drawable.arrow),
                    contentDescription = "arrow",
                    modifier = Modifier.size(70.dp)
                )
            }


            // 이번주 스트레스
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "이번 주 스트레스",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkNavy,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "82",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
            }
        }
    }
}