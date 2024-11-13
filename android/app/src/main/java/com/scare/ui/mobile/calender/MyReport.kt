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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.R
import com.scare.data.calender.repository.WeeklyReportRepository
import com.scare.ui.mobile.calender.component.CourseReport
import com.scare.ui.mobile.calender.component.WeeklyStressResult
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.viewmodel.calender.WeeklyReportViewModel
import com.scare.ui.mobile.viewmodel.calender.WeeklyReportViewModelFactory
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.Gray
import com.scare.ui.theme.NeonYellow
import com.scare.ui.theme.ScareTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MyReport(
    from: String?,
    to: String?,
    viewModel: WeeklyReportViewModel
) {
    val weeklyReport by viewModel.weeklyReport.collectAsState()

    LaunchedEffect(from, to) {
        if (from != null && to != null) {
            viewModel.fetchWeeklyReport(from, to)
        }
    }

    val stressData = weeklyReport?.data?.find { it.type == "STRESS" }
    val walkingData = weeklyReport?.data?.find { it.type == "WALKING" }

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

            WeeklyStressResult(lastStress = stressData?.lastWeekStress, currentStress = stressData?.currentWeekStress)

            Spacer(modifier = Modifier.size(12.dp))

            CourseReport(walkingData)

        }
    }
}
