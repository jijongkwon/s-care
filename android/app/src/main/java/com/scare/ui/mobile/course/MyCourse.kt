package com.scare.ui.mobile.course

import androidx.compose.foundation.layout.*
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
import com.scare.data.dto.Course.CourseResponseDTO
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.course.component.CourseList
import com.scare.ui.theme.ScareTheme
import com.scare.ui.theme.Typography

@Composable
fun MyCourse() {

    Scaffold(
        topBar = {
            TheHeader(isMainPage = false)
        }
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            val courseList: List<CourseResponseDTO> = listOf(
                CourseResponseDTO(
                    courseId = 46,
                    startedAt = "2024-11-01T16:42:23",
                    finishedAt = "2024-11-01T16:42:23",
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
                    finishedAt = "2024-11-01T16:42:22",
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
                    modifier = Modifier.padding(0.dp, 16.dp)
                )
                CourseList(courseList)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyCoursePreview() {
    ScareTheme {
        MyCourse()
    }
}