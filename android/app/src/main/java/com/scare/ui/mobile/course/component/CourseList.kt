package com.scare.ui.mobile.course.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.data.dto.Course.CourseResponseDTO
import com.scare.ui.theme.ScareTheme

@ExperimentalNaverMapApi
@Composable
fun CourseList(courseList: List<CourseResponseDTO>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        items(courseList) { course ->
            CourseItem(course)
        }
    }
}

@ExperimentalNaverMapApi
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CourseListPreview() {
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
    ScareTheme {
        CourseList(courseList)
    }
}