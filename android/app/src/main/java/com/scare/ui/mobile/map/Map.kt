package com.scare.ui.mobile.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.compose.*
import com.scare.ui.mobile.common.TheHeader

@ExperimentalNaverMapApi
@Composable
fun Map(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TheHeader(
                imageUrl = null,  // 메인 페이지가 아니므로 프로필 이미지는 null
                isMainPage = false,  // 뒤로 가기 버튼을 표시할지 결정
                navController = navController,  // NavController 전달
                onBackClick = { navController.popBackStack() } // 뒤로 가기 버튼 눌렀을 때의 동작
            ) // 뒤로 가기 버튼 눌렀을 때의 동작
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