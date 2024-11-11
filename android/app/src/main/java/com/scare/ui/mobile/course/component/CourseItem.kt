package com.scare.ui.mobile.course.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.data.course.dto.CourseResponseDTO
import com.scare.ui.theme.Typography
import com.scare.util.calculateTimeDifference
import com.scare.util.formatDateTimeToRender

@ExperimentalNaverMapApi
@Composable
fun CourseItem(course: CourseResponseDTO) {

    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(60.dp, 30.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 날짜와 시작 시간
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = formatDateTimeToRender(course.startedAt),
                        style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 총 시간
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = calculateTimeDifference(
                                course.startedAt,
                                course.finishedAt
                            ),
                            style = Typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "총 시간",
                            style = Typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded }),
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Detail",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.tertiary,
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (isExpanded) {
                CourseDetailMap(course)
            }
        }
    }
}