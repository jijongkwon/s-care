package com.scare

import GoogleLoginRepository
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.data.RetrofitClient
import com.scare.data.course.repository.CourseRepository
import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.dataStore.LastSaveData
import com.scare.data.heartrate.repository.StressRepository
import com.scare.data.member.repository.Auth.TokenRepository
import com.scare.data.member.repository.User.UserInfoRepository
import com.scare.service.listener.LogInListenerService
import com.scare.ui.mobile.calender.MyCalender
import com.scare.ui.mobile.calender.MyReport
import com.scare.ui.mobile.common.LocalCourseViewModel
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.course.MyCourse
import com.scare.ui.mobile.main.MainPage
import com.scare.ui.mobile.main.MyAuthPage
import com.scare.ui.mobile.main.StartPage
import com.scare.ui.mobile.map.Map
import com.scare.ui.mobile.viewmodel.course.CourseViewModel
import com.scare.ui.mobile.viewmodel.course.CourseViewModelFactory
import com.scare.ui.mobile.viewmodel.login.LoginViewModel
import com.scare.ui.mobile.viewmodel.login.LoginViewModelFactory
import com.scare.ui.mobile.viewmodel.sensor.HeartRateManager
import com.scare.ui.mobile.viewmodel.sensor.HeartRateViewModel
import com.scare.ui.mobile.viewmodel.stress.StressStoreManager
import com.scare.ui.mobile.viewmodel.stress.StressViewModel
import com.scare.ui.theme.ScareTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "scare mobile"

class MainActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tokenRepository: TokenRepository // 클래스 필드로 선언
    private val heartRateViewModel: HeartRateViewModel by viewModels()
    private lateinit var logInListenerService: LogInListenerService
    private lateinit var stressViewModel: StressViewModel // StressViewModel 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HeartRateManager.setViewModel(heartRateViewModel)

        // TokenRepository 초기화 및 RetrofitClient 초기화
        tokenRepository = TokenRepository.getInstance(this)
        RetrofitClient.init(tokenRepository)

        val googleLoginRepository = GoogleLoginRepository(this) // GoogleLoginRepository 초기화
        loginViewModel = ViewModelProvider(
            this, LoginViewModelFactory(googleLoginRepository, tokenRepository)
        )[LoginViewModel::class.java]

        // UserInfoRepository 생성
        val userInfoRepository = UserInfoRepository()

        // CourseRepository 인스턴스 생성
        val courseRepository = CourseRepository()

        // CourseViewModel 인스턴스 생성 (ViewModelProvider와 Factory 사용)
        val courseViewModel = ViewModelProvider(
            this,
            CourseViewModelFactory(courseRepository)
        )[CourseViewModel::class.java]

        // LogInListenerService 초기화
        logInListenerService = LogInListenerService(this)

        // AppDatabase를 통해 HeartRateDao 인스턴스 가져오기
        val heartRateDao = AppDatabase.getInstance(this).getHeartRateDao()

        val lastSaveData = LastSaveData(this)
        val stressStoreManager = StressStoreManager(this, heartRateDao)
        val stressRepository = StressRepository()

        stressViewModel = StressViewModel(lastSaveData, stressStoreManager, stressRepository)

        // StressViewModel의 데이터 업로드 메서드 호출
        stressViewModel.uploadDailyStressData()

        initializeFCM() // FCM 초기화

        setContent {
            val navController = rememberNavController()
            var isInitialized by remember { mutableStateOf(false) } // 초기화 상태 변수

            CompositionLocalProvider(
                LocalNavController provides navController,
                LocalCourseViewModel provides courseViewModel
            ) {
                ScareTheme {
                    // accessToken 상태를 관찰하여 실시간으로 업데이트 확인
                    val accessToken by tokenRepository.accessTokenFlow.collectAsState(initial = null)

                    // 초기화 완료 여부 설정
                    LaunchedEffect(accessToken) {
                        if (accessToken != null || accessToken == null) {
                            isInitialized = true // accessToken 상태가 안정화된 후에만 표시
                        }
                    }

                    if (isInitialized) {
                        // accessToken 상태에 따라 초기 시작 화면 설정
                        val startDestination = if (accessToken != null) "main" else "start"

                        @OptIn(ExperimentalNaverMapApi::class)
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable("start") { StartPage(loginViewModel) { launchLogin() } }
                            composable("main") { MainPage(loginViewModel, heartRateViewModel) }
                            composable("statistics") { MyCalender() } // "statistics" 경로 추가
                            composable("report?from={from}&to={to}") { backStackEntry ->
                                // from과 to를 정확히 가져옵니다.
                                val from = backStackEntry.arguments?.getString("from")
                                val to = backStackEntry.arguments?.getString("to")

                                MyReport(from = from, to = to) // MyReport에 매개변수 전달
                            }
                            composable("walk") { MyCourse() } // "walk" 경로 추가
                            composable("map") { Map() } // "map" 경로 추가
                            composable("mypage") { MyAuthPage(userInfoRepository, tokenRepository) } // "map" 경로 추가
                        }

                        // accessToken이 null일 경우 start로 이동
                        LaunchedEffect(accessToken) {
                            Log.d("MainActivity", "accessToken changed: $accessToken")
                            if (accessToken == null) {
                                navController.navigate("start") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                loginViewModel.handleSignInResult(data) // 로그인 결과 처리

                // 로그인 성공 후 워치에 로그인 상태 전송
                logInListenerService.sendAuthRequest(true)
            }
        }

    private fun launchLogin() {
        signInLauncher.launch(loginViewModel.getSignInIntent())
    }

    private fun initializeFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", "FCM Token: $token")

                // CoroutineScope를 사용하여 saveFcmToken 호출
                CoroutineScope(Dispatchers.IO).launch {
                    tokenRepository.saveFcmToken(token)
                }
            } else {
                Log.e("FCM Token", "Failed to get FCM token", task.exception)
            }
        }
    }
}