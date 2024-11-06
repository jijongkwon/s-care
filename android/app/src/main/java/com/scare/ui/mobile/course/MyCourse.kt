package com.scare.ui.mobile.course

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.data.dto.Course.CourseResponseDTO
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.course.component.CourseList
import com.scare.ui.theme.DarkColorScheme
import com.scare.ui.theme.LightColorScheme
import com.scare.ui.theme.ScareTheme
import com.scare.ui.theme.Typography

@ExperimentalNaverMapApi
@Composable
fun MyCourse() {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    Scaffold(
        topBar = {
            TheHeader(isMainPage = false)
        }
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            // TODO: 산책 코스 목록 조회 API 연동
            val courseList: List<CourseResponseDTO> = listOf(
                CourseResponseDTO(
                    courseId = 46,
                    startedAt = "2024-11-01T16:42:23",
                    finishedAt = "2024-11-01T17:21:04",
                    posList = null,
                    startIdx = 0,
                    endIdx = 0,
                    maxStress = 10.0,
                    minStress = 20.0,
                    distance = 10.0
                ),
                CourseResponseDTO(
                    courseId = 45,
                    startedAt = "2024-11-01T16:42:22",
                    finishedAt = "2024-11-01T19:17:58",
                    posList = null,
                    startIdx = 0,
                    endIdx = 0,
                    maxStress = 10.0,
                    minStress = 20.0,
                    distance = 10.0
                )
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "내 산책 코스",
                    style = Typography.titleLarge.copy( // TextStyle 적용
                        fontSize = 18.sp, // 크기 변경
                        color = MaterialTheme.colorScheme.onSurface, // 색깔 변경
                        fontWeight = FontWeight.Bold // 굵기 변경
                    ),
                    modifier = Modifier.padding(0.dp, 24.dp)
                )
                HorizontalDivider(
                    color = colorScheme.tertiary,
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                CourseList(courseList)
            }
        }
    }
}

@ExperimentalNaverMapApi
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyCoursePreview() {
    ScareTheme {
        MyCourse()
    }
}