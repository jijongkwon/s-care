package com.scare.ui.mobile.main

import GoogleLoginRepository
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.login.LoginViewModel
import com.scare.ui.mobile.main.component.ButtonContainer
import com.scare.ui.mobile.main.component.MyPetImage
import com.scare.ui.mobile.main.component.MyStressRate
import com.scare.ui.mobile.main.component.PetSentence
import com.scare.ui.mobile.main.component.SolutionCardList
import com.scare.ui.theme.ScareTheme

@Composable
fun MainPage(loginViewModel: LoginViewModel, navController: NavHostController) {

    val email by loginViewModel.email.observeAsState()
    val nickName by loginViewModel.nickName.observeAsState()
    val profileUrl by loginViewModel.profileUrl.observeAsState()

    Scaffold(
        topBar = { TheHeader(profileUrl, isMainPage = true) }
    ) { innerPadding ->
        val solutions = listOf("산책하기", "ASMR", "펫과 대화하기")
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PetSentence()
            MyPetImage(60)
            MyStressRate(60)
            ButtonContainer(navController)
            SolutionCardList(solutions)
        }
    }
}

///////////////프리뷰 확인
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StartPagePreview() {
    ScareTheme {
        MainPage(loginViewModel = LoginViewModel(GoogleLoginRepository(LocalContext.current)), navController = rememberNavController()) // 빈 콜백 함수 전달
    }
}