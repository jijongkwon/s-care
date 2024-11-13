package com.scare.ui.mobile.calender.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.scare.R
import com.scare.data.calender.dto.WeeklyReportDataDTO
import com.scare.ui.theme.Gray
import com.scare.ui.theme.NeonYellow

@ExperimentalNaverMapApi
@Composable
fun CourseReport(
    walkingData: WeeklyReportDataDTO?
) {
    var isExpanded by remember { mutableStateOf(true) }

    val totalTime = walkingData?.totalWalkingTime.toString()
    val walkCount = walkingData?.walkingCnt
    val averageStressChange = walkingData?.avgStressChange

    val startIdx = walkingData?.startIdx ?: 0
    val endIdx = walkingData?.endIdx ?: 0

    val coords = walkingData?.posList
        ?.let {
            val safeEndIdx = minOf(endIdx + 1, it.size)
            it.subList(startIdx, safeEndIdx).map { pos ->
                LatLng(pos.lat, pos.lng)
            }
        } ?: emptyList()


    // 위쪽 테두리
    Divider(color = NeonYellow, thickness = 0.75.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 상단 아이콘과 제목
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.happy_dog_face),
                        contentDescription = "Walk Icon",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "나의 산책 리포트",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                Icon(
                    modifier = Modifier.clickable { isExpanded = !isExpanded },
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand or Collapse",
                )
            }

            // 요약 정보 표시
            if (isExpanded) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = totalTime,
                                style = MaterialTheme.typography.titleLarge,
                                color = NeonYellow,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text("총 산책 시간", style = MaterialTheme.typography.bodySmall)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$walkCount",
                                style = MaterialTheme.typography.titleLarge,
                                color = NeonYellow,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text("산책 횟수", style = MaterialTheme.typography.bodySmall)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$averageStressChange",
                                style = MaterialTheme.typography.titleLarge,
                                color = NeonYellow,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Text("평균 변화", style = MaterialTheme.typography.bodySmall)
                        }
                }

                    Text(
                        text = "My Best Healing Course",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .border(width = 1.dp, color = Gray, shape = MaterialTheme.shapes.medium)
                    ) {
                        if (coords.isNotEmpty()) {
                            NaverMap(
                                modifier = Modifier.matchParentSize(),
                            ) {
                                // 전체 경로를 PathOverlay로 표시
                                PathOverlay(
                                    coords = coords,
                                    color = NeonYellow,
                                    width = 5.dp
                                )
                            }
                        } else {
                            // 지도 데이터가 없을 때 대체 메시지
                            Text(
                                text = "코스 데이터가 없습니다",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                }

            }
        }
    }

    // 위쪽 테두리
    Divider(color = NeonYellow, thickness = 0.75.dp)
}