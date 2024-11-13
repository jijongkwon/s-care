//package com.scare.handpressure.feature.handtracking.ui.components
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.drawscope.Stroke
//import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
//
//@Composable
//fun HandLandmarksOverlay(
//    result: List<NormalizedLandmark>?,
//    modifier: Modifier = Modifier
//) {
//    Box(modifier = modifier) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val scaleFactorX = 2f
//            val scaleFactorY = 1f
//
//            result?.forEachIndexed { index, landmark ->
//                val centerX = size.width / 2
//                val centerY = size.height / 2
//
//                val offsetX = 0f
//                val offsetY = -30f
//
//                val scaledX =
//                    centerX + (landmark.x() * size.width - centerX) * scaleFactorX + offsetX
//                val scaledY =
//                    centerY + (landmark.y() * size.height - centerY) * scaleFactorY + offsetY
//
//                drawCircle(
//                    color = Color.Green,
//                    radius = 12f,
//                    center = androidx.compose.ui.geometry.Offset(
//                        scaledX,
//                        scaledY
//                    ),
//                    style = Stroke(width = 3f)
//                )
//            }
//        }
//    }
//}
