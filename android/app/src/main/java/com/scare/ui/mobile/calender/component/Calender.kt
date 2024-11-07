package com.scare.ui.mobile.calender.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scare.R
import com.scare.ui.theme.NeonYellow
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

//달력
@Composable
fun Calender(modifier: Modifier = Modifier, onDaySelected: (LocalDate) -> Unit) {

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val today:LocalDate = LocalDate.now()
    var selectedDay by remember { mutableStateOf(today) }

    // "yyyy년 MM월" 형식으로 년/월 포맷터 정의
    val monthFormatter = DateTimeFormatter.ofPattern("yyyy년 M월")

    Column(modifier = Modifier.padding(16.dp)) {
        // 달력 상단: 년도와 월 표시
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon (
                Icons.Filled.KeyboardArrowLeft,
                contentDescription = "BackToLastMonth",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // 이전 달로 이동
                        currentMonth = currentMonth.minusMonths(1)
                    }
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = currentMonth.format(monthFormatter),
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.width(20.dp))

            Icon (
                Icons.Filled.KeyboardArrowRight,
                contentDescription = "Next Month",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // 다음 달로 이동
                        currentMonth = currentMonth.plusMonths(1)
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 달력: 요일 헤더
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
            for (day in daysOfWeek) {
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 달력: 날짜 그리드
        val daysInMonth = currentMonth.lengthOfMonth() // 현재 월의 총 일수
        val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7 // 첫 번째 날짜의 요일 (일요일부터 시작하도록 조정)

        LazyVerticalGrid (
            columns = GridCells.Fixed(7), // 7일 한 줄로 구성
            content = {
                // 첫 번째 날짜 이전에 빈 칸 추가
                for (i in 0 until firstDayOfMonth) {
                    item { Spacer(modifier = Modifier.size(40.dp)) }
                }

                // 날짜 추가
                for (day in 1..daysInMonth) {

                    item {
                        val date: LocalDate = currentMonth.atDay(day)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .size(75.dp)
                                .padding(4.dp)
                        ) {
                            if (date.isBefore(today)) {
                                Image(
                                    painter = painterResource(R.drawable.happy_dog_face),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                // 이미지가 없는 경우 빈 공간 유지
                                Spacer(modifier = Modifier.size(40.dp))
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // 날짜 아이템 추가
                            DayItem(
                                date = date,
                                isSelected = date == selectedDay,
                                onDaySelected = {
                                    selectedDay = it
                                    onDaySelected(it) // 날짜 선택 시 DayStress로 날짜 전달
                                }
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DayItem(date: LocalDate, isSelected: Boolean, onDaySelected: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(if (isSelected) NeonYellow else Color.Transparent)
            .clickable { onDaySelected(date) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = date.dayOfMonth.toString(), textAlign = TextAlign.Center)
    }
}