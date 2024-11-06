package com.scare.ui.mobile.course.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import com.scare.R
import com.scare.data.dto.Course.CourseResponseDTO
import com.scare.ui.theme.DarkColorScheme
import com.scare.ui.theme.LightColorScheme

@ExperimentalNaverMapApi
@Composable
fun CourseDetail(course: CourseResponseDTO) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
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

    // TODO: 산책 코스 상세 조회 API 연동
    //val coords = course.posList?.map { position -> LatLng(position.lat, position.lng) }
    val coords = listOf(
        LatLng(37.49764051593013, 127.03520144823648),
        LatLng(37.49764051593013, 127.03578938847163),
        LatLng(37.49820795635836, 127.03578938847163),
        LatLng(37.49820795635836, 127.03637732870678),
        LatLng(37.498775396786584, 127.03637732870678),
        LatLng(37.498775396786584, 127.03696526894193),
        LatLng(37.49934283721481, 127.03696526894193),
        LatLng(37.49934283721481, 127.03755320917708),
        LatLng(37.49991027764304, 127.03755320917708),
        LatLng(37.49991027764304, 127.03814114941223),
        LatLng(37.50047771807127, 127.03814114941223),
        LatLng(37.50047771807127, 127.03872908964738),
        LatLng(37.501045158499494, 127.03872908964738),
        LatLng(37.501045158499494, 127.03931702988253),
        LatLng(37.50161259892772, 127.03931702988253),
        LatLng(37.50161259892772, 127.03990497011768),
        LatLng(37.50218003935595, 127.03990497011768),
        LatLng(37.50218003935595, 127.04049291035282),
        LatLng(37.502747479784176, 127.04049291035282),
        LatLng(37.502747479784176, 127.04108085058797),
        LatLng(37.503314920212404, 127.04108085058797),
        LatLng(37.503314920212404, 127.04166879082312),
        LatLng(37.50388236064063, 127.04166879082312),
        LatLng(37.50388236064063, 127.04225673105827),
        LatLng(37.50444980106886, 127.04225673105827),
        LatLng(37.50444980106886, 127.04284467129342),
        LatLng(37.505017241497086, 127.04284467129342),
        LatLng(37.505017241497086, 127.04343261152857),
        LatLng(37.50558468192531, 127.04343261152857),
        LatLng(37.50558468192531, 127.04402055176372)

    )
    val firstCoord = coords[0]
    val lastCoord = coords[coords.size - 1]
    val centerCoord = LatLng(37.501329, 127.039611)

    val cameraPositionState = rememberCameraPositionState()
    val cameraUpdateCenter = CameraUpdate.scrollTo(centerCoord)
    val cameraUpdateZoom = CameraUpdate.zoomTo(15.0)
    cameraPositionState.move(cameraUpdateCenter)
    cameraPositionState.move(cameraUpdateZoom)

    Box(Modifier.fillMaxWidth().height(500.dp)) {
        NaverMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ) {
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
            PathOverlay(
                coords = coords,
                color = colorScheme.tertiary,
                outlineColor = colorScheme.tertiary,
                width = 5.dp,
            )
        }
    }
}