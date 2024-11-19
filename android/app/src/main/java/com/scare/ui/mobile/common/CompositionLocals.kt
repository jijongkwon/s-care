package com.scare.ui.mobile.common

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.scare.ui.mobile.viewmodel.course.CourseViewModel
import com.scare.ui.mobile.viewmodel.walk.WalkViewModel

val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

val LocalCourseViewModel = staticCompositionLocalOf<CourseViewModel?> { null }

val LocalWalkViewModel = staticCompositionLocalOf<WalkViewModel?> { null }