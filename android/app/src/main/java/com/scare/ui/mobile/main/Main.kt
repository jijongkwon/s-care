package com.scare.ui.mobile.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.main.component.ButtonContainer
import com.scare.ui.mobile.main.component.MyPetImage
import com.scare.ui.mobile.main.component.MyStressRate
import com.scare.ui.mobile.main.component.PetSentence
import com.scare.ui.mobile.main.component.SolutionCardList
import com.scare.ui.mobile.viewmodel.login.LoginViewModel
import com.scare.ui.mobile.viewmodel.pressure.Solution
import com.scare.ui.mobile.viewmodel.sensor.HeartRateViewModel


@Composable
fun MainPage(loginViewModel: LoginViewModel, heartRateViewModel: HeartRateViewModel) {

    val profileUrl by loginViewModel.profileUrl.collectAsState()
    val stress by heartRateViewModel.stress.collectAsState()

    val solutions = listOf(
        Solution("산책하기", "map"),
        Solution("ASMR", "asmr"),
        Solution("지압하기", "hand-tracking")  // 손 트래킹 경로 추가
    )

    Scaffold(
        topBar = { TheHeader(profileUrl, isMainPage = true) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PetSentence(stress)
            MyPetImage(stress)
            MyStressRate(stress)
            ButtonContainer()
            SolutionCardList(solutions)
        }
    }
}