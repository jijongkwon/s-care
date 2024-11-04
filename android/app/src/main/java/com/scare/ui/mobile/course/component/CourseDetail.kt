package com.scare.ui.mobile.course.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberFusedLocationSource

@ExperimentalNaverMapApi
@Composable
fun CourseDetail(modifier: Modifier) {
    Box(Modifier.fillMaxWidth().height(300.dp)) {
        NaverMap(
            locationSource = rememberFusedLocationSource(),
        )
    }
}