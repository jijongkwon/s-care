package com.scare.ui.mobile.calender

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.R
import com.scare.ui.mobile.calender.component.CourseReport
import com.scare.ui.mobile.calender.component.WeeklyStressResult
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.Gray
import com.scare.ui.theme.NeonYellow
import com.scare.ui.theme.ScareTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MyReport(from: String?, to: String?) {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(from, to) {
        // 주간 리포트 API 호출
//      fetchWeeklyReportData(from, to)
    }

    Scaffold (
        topBar = { TheHeader(null, isMainPage = false) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp), // 추가적인 내부 패딩 적용
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "$from 부터 $to"
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text="나의 주간 리포트",
                modifier = Modifier.padding(bottom = 12.dp),
                style = MaterialTheme.typography.titleLarge,
            )

            WeeklyStressResult()

            Spacer(modifier = Modifier.size(12.dp))

            CourseReport()

        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyReportPreview() {
    ScareTheme {
        MyReport(from = "20241001", to = "20241004")
    }
}