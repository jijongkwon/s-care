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
import com.scare.ui.mobile.main.component.*
import com.scare.ui.mobile.viewmodel.login.LoginViewModel
import com.scare.ui.mobile.viewmodel.sensor.HeartRateViewModel


@Composable
fun MainPage(loginViewModel: LoginViewModel, heartRateViewModel: HeartRateViewModel) {

    val profileUrl by loginViewModel.profileUrl.collectAsState()
    val stress by heartRateViewModel.stress.collectAsState()

    Scaffold(
        topBar = { TheHeader(profileUrl, isMainPage = true) }
    ) { innerPadding ->
        val solutions = listOf("산책하기", "ASMR")

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