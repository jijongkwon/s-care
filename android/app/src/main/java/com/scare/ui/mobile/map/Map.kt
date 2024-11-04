package com.scare.ui.mobile.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.naver.maps.map.compose.*
import com.scare.ui.mobile.common.TheHeader

@ExperimentalNaverMapApi
@Composable
fun Map() {

    Scaffold(
        topBar = {
            TheHeader(isMainPage = false)
        }
    ) { innerPadding ->
        Box(
            Modifier.fillMaxSize().padding(innerPadding)
        ) {
            var mapProperties by remember {
                mutableStateOf(
                    MapProperties(maxZoom = 20.0, minZoom = 5.0, locationTrackingMode = LocationTrackingMode.Follow)
                )
            }
            var mapUiSettings by remember {
                mutableStateOf(
                    MapUiSettings(isLocationButtonEnabled = true)
                )
            }
            NaverMap(
                locationSource = rememberFusedLocationSource(),
                properties = mapProperties,
                uiSettings = mapUiSettings
            )
        }
    }
}