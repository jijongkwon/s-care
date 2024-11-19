package com.scare.handpressure.feature.handtracking.ui

//@Composable
//fun HandTrackingScreen(
//    viewModel: HandTrackingViewModel = hiltViewModel()
//) {
//    val handLandmarkerResult by viewModel.handLandmarks.collectAsState()
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        CameraPreview(
//            modifier = Modifier.fillMaxSize(),
//            onFrameAvailable = { image ->
//                viewModel.processFrame(image)
//            }
//        )
//
//        HandLandmarksOverlay(
//            result = handLandmarkerResult,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}