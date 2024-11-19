/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.scare

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.scare.presentation.home.HomeAuth
import com.scare.presentation.home.HomeScreen
import com.scare.presentation.sensor.HeartRateManager
import com.scare.presentation.sensor.HeartRateViewModel
import com.scare.presentation.sensor.HeartRateViewModelFactory
import com.scare.presentation.theme.ScareTheme
import com.scare.service.listener.AuthRequestService
import com.scare.viewmodel.auth.AuthViewModel
import com.scare.viewmodel.auth.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var authRequestService: AuthRequestService

    // AuthViewModel 생성에 AuthViewModelFactory 사용
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(this)
    }

    private val heartRateViewModel: HeartRateViewModel by viewModels {
        HeartRateViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()

        HeartRateManager.setViewModel(heartRateViewModel)

        // AuthRequestService 인스턴스 생성
        authRequestService = AuthRequestService(this)

        // 로그인 상태 요청
        authRequestService.sendAuthRequest()

        setContent {
            // 로그인 상태를 관찰하여 UI를 업데이트
            val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)
            val homeRoot = stringResource(R.string.home_root)
            val authRoot = stringResource(R.string.auth_root)

            val startDestination = if (!isLoggedIn) authRoot else homeRoot

            Log.d("MainActivity", "$isLoggedIn")

            ScareTheme {
                AppScaffold {
                    val navController = rememberSwipeDismissableNavController()
                    SwipeDismissableNavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(route = homeRoot) {
                            HomeScreen(
                                heartRateViewModel,
                            )
                        }
                        composable(route = authRoot) {
                            HomeAuth {
                                authRequestService.sendAuthRequest()
                            }
                        }
                    }
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
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
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    }
}