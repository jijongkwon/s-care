package com.scare

import GoogleLoginRepository
import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.data.RetrofitClient
import com.scare.data.calender.repository.MonthlyStressRepository
import com.scare.data.calender.repository.WeeklyReportRepository
import com.scare.data.course.repository.CourseRepository
import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.dataStore.LastSaveData
import com.scare.data.heartrate.repository.StressRepository
import com.scare.data.location.database.LocationDatabase
import com.scare.data.member.repository.Auth.TokenRepository
import com.scare.data.member.repository.User.UserInfoRepository
import com.scare.data.walk.repository.WalkRepository
import com.scare.handpressure.feature.pressure.ui.HandPressureScreen
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.repository.location.LocationRepository
import com.scare.service.listener.LogInListenerService
import com.scare.ui.mobile.calender.MyCalender
import com.scare.ui.mobile.calender.MyReport
import com.scare.ui.mobile.common.LocalCourseViewModel
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.common.LocalWalkViewModel
import com.scare.ui.mobile.course.MyCourse
import com.scare.ui.mobile.main.MainPage
import com.scare.ui.mobile.main.MyAuthPage
import com.scare.ui.mobile.main.StartPage
import com.scare.ui.mobile.map.Map
import com.scare.ui.mobile.viewmodel.calender.MonthlyStressViewModel
import com.scare.ui.mobile.viewmodel.calender.MonthlyStressViewModelFactory
import com.scare.ui.mobile.viewmodel.calender.WeeklyReportViewModel
import com.scare.ui.mobile.viewmodel.calender.WeeklyReportViewModelFactory
import com.scare.ui.mobile.viewmodel.course.CourseViewModel
import com.scare.ui.mobile.viewmodel.course.CourseViewModelFactory
import com.scare.ui.mobile.viewmodel.login.LoginViewModel
import com.scare.ui.mobile.viewmodel.login.LoginViewModelFactory
import com.scare.ui.mobile.viewmodel.sensor.HeartRateManager
import com.scare.ui.mobile.viewmodel.sensor.HeartRateViewModel
import com.scare.ui.mobile.viewmodel.stress.StressStoreManager
import com.scare.ui.mobile.viewmodel.stress.StressViewModel
import com.scare.ui.mobile.viewmodel.walk.WalkViewModel
import com.scare.ui.mobile.viewmodel.walk.WalkViewModelFactory
import com.scare.ui.theme.ScareTheme
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "scare mobile"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tokenRepository: TokenRepository // 클래스 필드로 선언
    private val heartRateViewModel: HeartRateViewModel by viewModels()
    private lateinit var logInListenerService: LogInListenerService
    private lateinit var stressViewModel: StressViewModel // StressViewModel 추가
    private lateinit var monthlyStressViewModel: MonthlyStressViewModel
    private lateinit var weeklyReportViewModel: WeeklyReportViewModel

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Camera permission granted")
        } else {
            Log.d(TAG, "Camera permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        HeartRateManager.setViewModel(heartRateViewModel)

        // 카메라 권한 요청
        requestCameraPermission.launch(Manifest.permission.CAMERA)

        // TokenRepository 초기화 및 RetrofitClient 초기화
        tokenRepository = TokenRepository.getInstance(this)
        RetrofitClient.init(tokenRepository)

        val googleLoginRepository = GoogleLoginRepository(this)
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

        val hearRateDb = AppDatabase.getInstance(this)
        val locationDb = LocationDatabase.getInstance(this)

        val heartRateRepository = HeartRateRepository(hearRateDb)
        val locationRepository = LocationRepository(locationDb)
        val walkRepository = WalkRepository()

        val walkViewModel = ViewModelProvider(
            this,
            WalkViewModelFactory(this, heartRateRepository, locationRepository, walkRepository)
        ).get(WalkViewModel::class.java)


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

        //한달 스트레스
        // MonthlyStressRepository 생성
        val monthlyStressRepository = MonthlyStressRepository()

        // MonthlyStressViewModel을 Factory를 사용하여 초기화
        val factory = MonthlyStressViewModelFactory(monthlyStressRepository)
        monthlyStressViewModel =
            ViewModelProvider(this, factory)[MonthlyStressViewModel::class.java]

        // MonthlyStressRepository 생성
        val weeklyReportRepository = WeeklyReportRepository()

        // MonthlyStressViewModel을 Factory를 사용하여 초기화
        val weeklyFactory = WeeklyReportViewModelFactory(weeklyReportRepository)
        weeklyReportViewModel =
            ViewModelProvider(this, weeklyFactory)[WeeklyReportViewModel::class.java]


        setContent {
            val navController = rememberNavController()
            var isInitialized by remember { mutableStateOf(false) }

            CompositionLocalProvider(
                LocalNavController provides navController,
                LocalCourseViewModel provides courseViewModel,
                LocalWalkViewModel provides walkViewModel
            ) {
                ScareTheme {
                    // accessToken 상태를 관찰하여 실시간으로 업데이트 확인
                    val accessToken by tokenRepository.accessTokenFlow.collectAsState(initial = null)

                    // 초기화 완료 여부 설정
                    LaunchedEffect(accessToken) {
                        if (accessToken != null || accessToken == null) {
                            isInitialized = true
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
                            // 기존 라우트들
                            composable("start") { StartPage(loginViewModel) { launchLogin() } }
                            composable("main") { MainPage(loginViewModel, heartRateViewModel) }
                            composable("statistics") { MyCalender(monthlyStressViewModel) } // "statistics" 경로 추가
                            composable("report?from={from}&to={to}") { backStackEntry ->
                                // from과 to를 정확히 가져옵니다.
                                val from = backStackEntry.arguments?.getString("from")
                                val to = backStackEntry.arguments?.getString("to")
                                MyReport(
                                    from = from,
                                    to = to,
                                    viewModel = weeklyReportViewModel
                                ) // MyReport에 매개변수 전달
                            }
                            composable("walk") { MyCourse() }
                            composable("map") { Map(this@MainActivity) }
                            composable("mypage") { MyAuthPage(userInfoRepository, tokenRepository) }

                            // 손 트래킹 화면 추가
                            composable("hand-tracking") {
                                HandPressureScreen(
                                    onComplete = {
                                        // 완료 시 다음 화면으로 이동하거나 필요한 동작 수행
                                        navController.navigate("main") {
                                            popUpTo("hand-pressure") { inclusive = true }
                                        }
                                    }
                                )
                            }
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

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { permission ->
                when {
                    permission.value -> {
                        Log.d(TAG, "permission granted")
                    }

                    shouldShowRequestPermissionRationale(permission.key) -> {
                        Log.d(TAG, "permission required")
                    }

                    else -> {
                        Log.d(TAG, "permission denied")
                    }
                }
            }
        }

    private fun checkPermission() {
        val isAllPermissionGranted = PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == 0
        }

        if (!isAllPermissionGranted) {
            requestPermissionLauncher.launch(PERMISSIONS)
        }
    }

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
        )
    }
}