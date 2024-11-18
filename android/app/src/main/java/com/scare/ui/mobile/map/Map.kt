package com.scare.ui.mobile.map

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import com.scare.ui.mobile.common.LocalWalkViewModel
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.map.component.StartWalkButton
import com.scare.ui.mobile.map.component.WalkEndModal
import com.scare.ui.theme.Gray
import com.scare.util.calculateTimeDifference
import com.scare.util.convertToMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

@ExperimentalNaverMapApi
@Composable
fun Map(context: Context) {

    val localWalkViewModel = LocalWalkViewModel.current

    val startTime by localWalkViewModel!!.walkStartTime.collectAsState()
    val locations by localWalkViewModel!!.locations.collectAsState()
    var currentTime = remember {
        mutableStateOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
    }
    val duration = remember(currentTime.value, startTime) {
        derivedStateOf {
            if (startTime !== "") {
                calculateTimeDifference(convertToMillis(startTime), currentTime.value).seconds
            } else {
                0
            }
        }
    }

    val isWalk by localWalkViewModel!!.isWalk.collectAsState()

    var isModalOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val MINIMUM_WALK_TIME = 180 // 최소 산책 시간(초)
    val LOADING_TIME = 3000L // 최소 로딩 시간(밀리초)

    LaunchedEffect(Unit) {
        localWalkViewModel!!.resetLocations()
    }

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
            val coroutineScope = rememberCoroutineScope()
            NaverMap(
                locationSource = rememberFusedLocationSource(),
                properties = mapProperties,
                uiSettings = mapUiSettings
            ) {
                if (locations.size >= 2) {
                    ArrowheadPathOverlay(
                        coords = locations.map { locations ->
                            LatLng(
                                locations.latitude,
                                locations.longitude
                            )
                        },
                        color = Gray,
                        outlineColor = Gray,
                        width = 5.dp,
                        globalZIndex = 0
                    )
                }
            }
            StartWalkButton(
                modifier = Modifier.fillMaxSize(),
                text = if (!isWalk) {
                    "저와 함께\n산책가요"
                } else {
                    "산책이 끝나면\n저를 눌러주세요"
                },
                onClick = {
                    currentTime.value =
                        LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                    if (!isWalk) {
                        localWalkViewModel!!.handleWalkStart(context)
                    } else {
                        coroutineScope.launch {
                            isModalOpen = true
                            if (duration.value >= MINIMUM_WALK_TIME) {
                                isLoading = true
                                localWalkViewModel!!.handleWalkEnd(context, true)
                                delay(LOADING_TIME)
                            }
                            isLoading = false
                        }
                    }
                }
            )
            if (isModalOpen) {
                WalkEndModal(
                    modifier = Modifier.fillMaxSize(),
                    onClose = { isModalOpen = false },
                    handleWalkStop = {
                        localWalkViewModel!!.handleWalkEnd(context, false)
                        isModalOpen = false
                    },
                    isWalkComplete = (duration.value >= MINIMUM_WALK_TIME),
                    isLoading = isLoading
                )
            }
        }
    }
}