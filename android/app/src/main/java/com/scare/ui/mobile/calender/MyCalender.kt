package com.scare.ui.mobile.calender

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scare.ui.mobile.calender.component.Calender
import com.scare.ui.mobile.calender.component.DayStress
import com.scare.ui.mobile.calender.component.WeeklyReport
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.theme.ScareTheme
import java.time.LocalDate

@Composable
fun MyCalender() {
    val navController = LocalNavController.current
    var selectedStress by remember { mutableStateOf(0.0) } // 선택된 날짜의 스트레스 상태
    var stressData by remember { mutableStateOf<Map<LocalDate, Double>>(emptyMap()) } // 한 달의 스트레스 데이터를 저장할 상태

    // API 요청으로 이 달의 스트레스 데이터를 가져오는 로직 (가상의 API 호출)
    LaunchedEffect(Unit) {
        // 예시 API 호출 후 데이터 저장
//        stressData = fetchMonthlyStressData()
    }

    Scaffold (
        topBar = { TheHeader(null, isMainPage = false) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp), // 추가로 패딩을 적용,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 달력에서 선택된 날짜의 스트레스를 받아옴
            Calender(modifier = Modifier,
                onDaySelected = {selectedDate ->
                selectedStress = stressData[selectedDate] ?: 0.0
            })

            Spacer(modifier = Modifier.height(16.dp))

            // 선택된 날짜의 스트레스 표시
            DayStress(modifier = Modifier, stress = selectedStress)

            Spacer(modifier = Modifier.height(16.dp))

            WeeklyReport(onClick={navController?.navigate("map")})
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CalenderPreview() {
    ScareTheme {
        MyCalender()
    }
}