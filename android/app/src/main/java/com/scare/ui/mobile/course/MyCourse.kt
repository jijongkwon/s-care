package com.scare.ui.mobile.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.ui.mobile.common.LocalCourseViewModel
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.course.component.CourseList
import com.scare.ui.theme.Typography

@ExperimentalNaverMapApi
@Composable
fun MyCourse() {

    val localCourseViewModel = LocalCourseViewModel.current

    val courseList by localCourseViewModel!!.courseList.collectAsState()

    LaunchedEffect(Unit) {
        // TODO: paging 처리
        localCourseViewModel!!.fetchWalkingCourseList(0, 100)
    }

    Scaffold(
        topBar = {
            TheHeader(isMainPage = false)
        }
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "내 산책 코스",
                    style = Typography.titleLarge.copy( // TextStyle 적용
                        fontSize = 18.sp, // 크기 변경
                        color = MaterialTheme.colorScheme.onSurface, // 색깔 변경
                        fontWeight = FontWeight.Bold // 굵기 변경
                    ),
                    modifier = Modifier.padding(0.dp, 24.dp)
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.tertiary,
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                CourseList(courseList)
            }
        }
    }
}
