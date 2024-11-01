package com.scare

import GoogleLoginRepository
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.scare.data.repository.Auth.TokenRepository
import com.scare.ui.mobile.calender.MyCalender
import com.scare.ui.mobile.course.MyCourse
import com.scare.ui.mobile.login.LoginActivity
import com.scare.ui.mobile.login.LoginViewModel
import com.scare.ui.mobile.login.LoginViewModelFactory

import com.scare.ui.mobile.main.MainPage
import com.scare.ui.mobile.main.StartPage
import com.scare.ui.theme.ScareTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            loginViewModel.handleSignInResult(data) // 로그인 결과 처리
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TokenRepository를 초기화하여 Context 전달
        TokenRepository.init(this)

        val googleLoginRepository = GoogleLoginRepository(this) // GoogleLoginRepository 초기화
        loginViewModel = ViewModelProvider(
            this, LoginViewModelFactory(googleLoginRepository, TokenRepository)
        )[LoginViewModel::class.java]

        // accessToken 확인 후 시작 화면 설정
        CoroutineScope(Dispatchers.Main).launch {
            val startDestination = if (TokenRepository.getAccessToken() != null) {
                "main"
            } else {
                "start"
            }

            setContent {
                ScareTheme {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("start") { StartPage(navController, loginViewModel) { launchLogin() } }
                        composable("main") { MainPage(loginViewModel, navController) }
                    }
                }
            }
        }
    }

    private fun launchLogin() {
        signInLauncher.launch(loginViewModel.getSignInIntent())
    }
}

//@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun ScarePreview() {
//    ScareTheme {
//        StartPage(navController = rememberNavController())
//    }
//}