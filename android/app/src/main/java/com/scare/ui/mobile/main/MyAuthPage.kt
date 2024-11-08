package com.scare.ui.mobile.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.scare.data.member.dto.User.UserInfoResponseDTO
import com.scare.data.member.repository.User.UserInfoRepository
import com.scare.service.listener.LogoutListenerService
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.viewmodel.user.UserInfoViewModel
import com.scare.ui.mobile.viewmodel.user.UserInfoViewModelFactory
import com.scare.ui.theme.Typography

@Composable
fun MyAuthPage(
    userInfoRepository: UserInfoRepository, // Repository를 파라미터로 전달받음
) {

    // ViewModelFactory를 사용하여 ViewModel을 생성
    val userInfoViewModel: UserInfoViewModel = viewModel(
        factory = UserInfoViewModelFactory(userInfoRepository)
    )

    // userInfo 데이터를 수집
    val userInfo by userInfoViewModel.userInfo.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val response = userInfoViewModel.fetchUserInfo() // 페이지 진입 시 API 호출
        Log.d("MyAuthPage", "response: $response")
    }

    Scaffold(
        topBar = { TheHeader(null, isMainPage = false) }
    ) { innerPadding ->

        // userInfo를 사용해 UI 표시
        userInfo?.let {
            MyAuthInfo(it, userInfoViewModel,context, modifier = Modifier.padding(innerPadding)) // MyAuthInfo에 데이터 전달
        } ?: Text("Loading...") // 데이터 로딩 중 표시
    }
}

@Composable
fun MyAuthInfo(
    userInfo: UserInfoResponseDTO,
    userInfoViewModel: UserInfoViewModel,
    context: Context,
    modifier: Modifier = Modifier,
) {

    val profileUrl = userInfo.profileUrl
    val nickname = userInfo.nickname
    val email = userInfo.email

    val navController = LocalNavController.current

    Column(
        modifier = modifier
            .fillMaxWidth() // 가로를 꽉 채움
            .fillMaxHeight(), // 세로를 꽉 채움
        verticalArrangement = Arrangement.spacedBy(21.dp, Alignment.CenterVertically), // 수직 방향 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally // 수평 방향 가운데 정렬
    ) {
        AsyncImage(
            model = profileUrl,
            contentDescription = "User Image",
            modifier = Modifier
                .size(100.dp).clip(CircleShape),
            contentScale = ContentScale.Crop

        )

        Text(
            text = "$nickname 님",
            style = Typography.titleLarge.copy( // TextStyle 적용
                fontSize = 24.sp, // 크기 변경
                fontWeight = FontWeight.Bold // 굵기 변경
            )
        )

        Text("$email")

        Column(
            modifier = modifier.padding(top = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically), // 수직 방향 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    userInfoViewModel.logout()
                    sendLogoutToWatch(context)
                    navController?.navigate("start")
                          },
            ) {
                Text(
                    text = "로그아웃",
                    style = Typography.titleLarge.copy( // TextStyle 적용
                        fontSize = 18.sp, // 크기 변경
                        fontWeight = FontWeight.Bold // 굵기 변경
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "탈퇴하기",
                style = Typography.titleLarge.copy( // TextStyle 적용
                    fontSize = 16.sp, // 크기 변경
                    fontWeight = FontWeight.Medium // 굵기 변경
                ),
                modifier = Modifier.clickable(onClick = {
                    userInfoViewModel.withdraw()
                    sendLogoutToWatch(context)
                    navController?.navigate("start")
                })
            )
        }
    }
}

// 로그아웃 또는 탈퇴 후 워치에 로그아웃 상태 전송
fun sendLogoutToWatch(context: Context) {
    val logoutListenerService = LogoutListenerService(context)
    logoutListenerService.sendAuthRequest(false)
}