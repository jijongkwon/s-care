package com.scare.ui.mobile.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap

@ExperimentalNaverMapApi
@Composable
fun Map(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        NaverMap(modifier = Modifier.fillMaxSize()) {}
    }
}