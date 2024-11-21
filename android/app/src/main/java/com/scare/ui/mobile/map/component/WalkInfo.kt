package com.scare.ui.mobile.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scare.ui.theme.Typography
import com.scare.util.formatDuration

@Composable
fun WalkInfo(
    modifier: Modifier,
    duration: State<Long>
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "산책 시간",
                    color = MaterialTheme.colorScheme.background,
                )
                Text(
                    text = formatDuration(duration.value),
                    color = MaterialTheme.colorScheme.background,
                    style = Typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold, fontSize = 24.sp
                    )
                )
            }
        }
    }
}