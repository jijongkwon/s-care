package com.scare.ui.mobile.map

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.naver.maps.map.compose.*
import com.scare.ui.mobile.common.LocalWalkViewModel
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.map.component.StartWalkButton
import com.scare.ui.mobile.map.component.WalkEndModal

@ExperimentalNaverMapApi
@Composable
fun Map(context: Context) {

    val localWalkViewModel = LocalWalkViewModel.current

    val isWalk by localWalkViewModel!!.isWalk.collectAsState()

    var isWalkEnd by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TheHeader(isMainPage = false)
    }) { innerPadding ->
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
                locationSource = rememberFusedLocationSource(), properties = mapProperties, uiSettings = mapUiSettings
            )
            StartWalkButton(
                modifier = Modifier.fillMaxSize(),
                text = if (!isWalk) {
                    "저와 함께\n산책가요"
                } else {
                    "산책이 끝나면\n저를 눌러주세요"
                },
                onClick = {
                    if (!isWalk) {
                        localWalkViewModel!!.handleWalkStart(context)
                    } else {
                        localWalkViewModel!!.handleWalkEnd(context)
                        isWalkEnd = true
                    }
                }
            )
            if (isWalkEnd) {
                WalkEndModal(
                    modifier = Modifier.fillMaxSize(),
                    onClose = { isWalkEnd = false }
                )
            }
        }
    }
}