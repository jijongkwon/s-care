package com.scare.ui.mobile.course.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import com.scare.R
import com.scare.data.course.dto.CourseResponseDTO
import com.scare.ui.mobile.common.LocalCourseViewModel
import com.scare.ui.theme.Gray

@ExperimentalNaverMapApi
@Composable
fun CourseDetailMap(course: CourseResponseDTO) {

    val localCourseViewModel = LocalCourseViewModel.current

    val courseDetail by localCourseViewModel!!.courseDetail.collectAsState()

    LaunchedEffect(Unit) {
        localCourseViewModel!!.fetchWalkingCourseDetail(course.courseId)
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

    Box(Modifier.fillMaxWidth().height(600.dp)) {
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
                ArrowheadPathOverlay(
                    coords = coords,
                    color = Gray,
                    outlineColor = Gray,
                    width = 5.dp,
                    globalZIndex = 0
                )
            }
        }
        CourseDetailInfo(
            modifier = Modifier.fillMaxSize(), course
        )
    }
}