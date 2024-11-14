package com.scare.ui.mobile.calender.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.scare.R
import com.scare.ui.mobile.viewmodel.calender.MonthlyStressViewModel
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.NeonYellow
import com.scare.ui.theme.White
import com.scare.util.getPetFace
import com.scare.util.getStressColor
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

//달력
@Composable
fun Calender(
    modifier: Modifier = Modifier,
    onDaySelected: (LocalDate) -> Unit,
    monthlyStressViewModel: MonthlyStressViewModel
) {

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val today:LocalDate = LocalDate.now()
    var selectedDay by remember { mutableStateOf(today) }

    // "yyyy년 MM월" 형식으로 년/월 포맷터 정의
    val monthFormatter = DateTimeFormatter.ofPattern("yyyy년 M월")

    // ViewModel의 stressLevelMap을 구독
    val stressLevelMap by monthlyStressViewModel.stressLevelMap.collectAsState()
    var isDataLoaded by remember { mutableStateOf(false) } // 데이터 로딩 상태 확인 플래그

    // API 호출하여 스트레스 데이터 가져오기
    LaunchedEffect(currentMonth) {
        // 여기서 currentMonth에 맞는 from과 to 값을 생성하여 API 호출
        val from = "${currentMonth.year}${String.format("%02d", currentMonth.monthValue)}01"
        val to = if (currentMonth == YearMonth.from(today)) {
            today.toString().replace("-", "")
        } else {
            "${currentMonth.year}${String.format("%02d", currentMonth.monthValue)}${currentMonth.lengthOfMonth()}"
        }

        Log.d("Calender", "from: $from, to: $to")

        // API 호출하여 데이터를 stressLevelMap에 저장
        monthlyStressViewModel.fetchMonthlyStressData(from, to)
        isDataLoaded = true // 데이터 로딩 완료로 표시
    }

    Log.d("Calender", "Received stressLevelMap: $stressLevelMap")

    if (isDataLoaded && stressLevelMap.isNotEmpty()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 달력 상단: 년도와 월 표시
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
                            val stress = stressLevelMap[date] ?: -1

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .size(75.dp)
                                    .padding(4.dp)
                            ) {
                                if (date.isBefore(today)) {
                                    val (color, hasBorder) = getStressColor(stress)

                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .background(color = color,
                                                shape = MaterialTheme.shapes.small)
                                            .then(
                                                if (hasBorder) Modifier.border(0.75.dp, White, shape = MaterialTheme.shapes.small)
                                                else Modifier
                                            )
                                    )
                                } else {
                                    // 이미지가 없는 경우 빈 공간 유지
                                    Spacer(modifier = Modifier.size(30.dp))
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
    } else {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingAnimation(modifier = Modifier.size(500.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text("로딩 중...", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun DayItem(date: LocalDate, isSelected: Boolean, onDaySelected: (LocalDate) -> Unit) {
    val backgroundColor = if (isSelected) NeonYellow else Color.Transparent
    val textColor = if (isSelected) DarkNavy else White // DarkNavy 색상 지정 (예: #001F3F)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(50.dp)
            .background(backgroundColor)
            .clickable { onDaySelected(date) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

//로딩중
@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // GIF를 위한 ImageLoader 설정
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(ImageDecoderDecoder.Factory())
        }
        .build()

    // GIF 리소스를 위한 ImageRequest 생성
    val imageRequest = ImageRequest.Builder(context)
        .data(R.drawable.walkinggif)
        .build()

    // Coil의 AsyncImage를 사용하여 GIF 표시
    Image(
        painter = rememberAsyncImagePainter(
            model = imageRequest,
            imageLoader = imageLoader
        ),
        contentDescription = "Loading Animation",
        modifier = modifier
    )
}
