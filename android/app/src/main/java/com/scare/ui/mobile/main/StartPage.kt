package com.scare.ui.mobile.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import com.scare.R

@Composable
fun StartPage(navController: NavHostController) {
    Box {
        StartImage(modifier = Modifier.fillMaxSize())
        Button(
            onClick = {
                // 로그인 로직 실행
                val imageUrl = "로그인 후 받은 imageUrl"
                val accessToken = "로그인 후 받은 accessToken"

                // NavController를 이용해 인수를 URI로 전달
                navController.navigate("main?imageUrl=$imageUrl&accessToken=$accessToken")
            },
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = LocalConfiguration.current.screenHeightDp.dp * 0.25f) // 세로 위치 조정
        ) {
            Text("구글 계정으로 가입")
        }
    }
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