package com.scare.ui.mobile.course.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.data.course.dto.CourseResponseDTO

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