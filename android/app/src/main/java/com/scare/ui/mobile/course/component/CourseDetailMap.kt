package com.scare.ui.mobile.course.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import com.scare.R
import com.scare.data.course.dto.CourseResponseDTO
import com.scare.ui.mobile.calender.component.LoadingAnimation
import com.scare.ui.mobile.common.LocalCourseViewModel
import com.scare.ui.theme.Gray

@ExperimentalNaverMapApi
@Composable
fun CourseDetailMap(course: CourseResponseDTO) {

    val localCourseViewModel = LocalCourseViewModel.current

    val courseDetail by localCourseViewModel!!.courseDetail.collectAsState()
    var isDataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        localCourseViewModel!!.fetchWalkingCourseDetail(course.courseId)
        isDataLoaded = true
    }

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(lightness = -0.2f)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isScrollGesturesEnabled = false, isZoomControlEnabled = false)
        )
    }

    val coords = if (courseDetail != null) {
        courseDetail!!.posList!!.map { position -> LatLng(position.lat, position.lng) }
    } else {
        listOf()
    }

    val cameraPositionState = rememberCameraPositionState()

    if (isDataLoaded) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            NaverMap(
                properties = mapProperties,
                uiSettings = mapUiSettings,
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    if (coords.isNotEmpty()) {
                        val bounds = LatLngBounds.Builder().apply {
                            coords.forEach { include(it) }
                        }.build()

                        val cameraUpdate = CameraUpdate.fitBounds(bounds, 100, 600, 100, 100)
                        cameraPositionState.move(cameraUpdate)
                    }
                }
            ) {
                if (coords.isNotEmpty()) {
                    val firstCoord = coords.first()
                    val lastCoord = coords.last()
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                firstCoord.latitude, firstCoord.longitude
                            )
                        ),
                        icon = OverlayImage.fromResource(R.drawable.first_marker)
                    )
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                lastCoord.latitude, lastCoord.longitude
                            )
                        ),
                        icon = OverlayImage.fromResource(R.drawable.last_marker)
                    )
                    if (coords.size >= 2) {
                        ArrowheadPathOverlay(
                            coords = coords,
                            color = Gray,
                            outlineColor = Gray,
                            width = 5.dp,
                            globalZIndex = 0
                        )
                    }
                    if (course.startIdx < coords.size &&
                        course.endIdx < coords.size &&
                        course.startIdx <= course.endIdx
                    ) {
                        val bestCourse = coords.slice(course.startIdx..course.endIdx)
                        if (bestCourse.size >= 2) {
                            PathOverlay(
                                coords = bestCourse,
                                color = MaterialTheme.colorScheme.tertiary,
                                outlineColor = MaterialTheme.colorScheme.tertiary,
                                width = 5.dp,
                                globalZIndex = 1
                            )
                        }
                    }
                }
            }
            CourseDetailInfo(
                modifier = Modifier.fillMaxSize(), course
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingAnimation(modifier = Modifier.size(500.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text("로딩 중...", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}