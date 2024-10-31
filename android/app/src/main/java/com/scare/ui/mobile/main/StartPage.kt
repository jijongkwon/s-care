package com.scare.ui.mobile.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.scare.R
import com.scare.ui.mobile.login.LoginViewModel

@Composable
fun StartPage(navController: NavHostController, loginViewModel: LoginViewModel) {
    val loginResult by loginViewModel.loginResult.observeAsState()

    // 로그인 성공 시 MainPage로 이동
    loginResult?.let { account ->
        val email by loginViewModel.email.observeAsState()
        val nickName by loginViewModel.nickName.observeAsState()
        val profileUrl by loginViewModel.profileUrl.observeAsState()

        if (email != null && nickName != null) {
            navController.navigate("main")
        }
    }

    Box {
        StartImage(modifier = Modifier.fillMaxSize())

        GoogleLoginButton(
            onClick = {
                // 로그인 로직 실행
                val imageUrl = "로그인 후 받은 imageUrl"
                val accessToken = "로그인 후 받은 accessToken"

                navController.navigate("main")
            },
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = LocalConfiguration.current.screenHeightDp.dp * 0.25f) // 세로 위치 조정
        )
    }
}

@Composable
fun GoogleLoginButton(onClick: () -> Unit, modifier: Modifier) {
    val googleLoginButton = painterResource(R.drawable.google_login);

    Image (
        painter = googleLoginButton,
        contentDescription = "구글 로그인",
        contentScale = ContentScale.Crop, // 이미지 화면 비율에 맞게 자르기
        modifier = modifier.clickable { onClick() } //
    )
}

@Composable
fun StartImage(modifier: Modifier = Modifier) {
    val startImage = painterResource(R.drawable.startpage)

    Image(
        painter = startImage,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop // 이미지 화면 비율에 맞게 자르기
    )
}