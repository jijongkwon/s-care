/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.scare

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.horologist.compose.layout.AppScaffold
import com.scare.presentation.home.HomeAuth
import com.scare.presentation.home.HomeScreen
import com.scare.presentation.sensor.HeartRateManager
import com.scare.presentation.sensor.HeartRateViewModel
import com.scare.presentation.sensor.HeartRateViewModelFactory
import com.scare.presentation.theme.ScareTheme
import com.scare.presentation.walk.WalkScreen
import com.scare.presentation.walk.WalkViewModel
import com.scare.presentation.walk.WalkViewModelFactory
import com.scare.service.listener.AuthRequestService
import com.scare.viewmodel.auth.AuthViewModel
import com.scare.viewmodel.auth.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var authRequestService: AuthRequestService

    // AuthViewModel 생성에 AuthViewModelFactory 사용
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(this)
    }

    private val walkViewModel: WalkViewModel by viewModels {
        WalkViewModelFactory(this)
    }

    private val heartRateViewModel: HeartRateViewModel by viewModels {
        HeartRateViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HeartRateManager.setViewModel(heartRateViewModel)

        // AuthRequestService 인스턴스 생성
        authRequestService = AuthRequestService(this)

        // 로그인 상태 요청
        authRequestService.sendAuthRequest()

        setContent {
            // 로그인 상태를 관찰하여 UI를 업데이트
            val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)
            val isWalk by walkViewModel.isWalk.collectAsState(initial = false)
            val homeRoot = stringResource(R.string.home_root)
            val walkRoot = stringResource(R.string.walk_root)
            val authRoot = stringResource(R.string.auth_root)

            val startDestination = if (!isLoggedIn) authRoot else if (isWalk) walkRoot else homeRoot

            Log.d("MainActivity", "$isLoggedIn")

            ScareTheme {
                AppScaffold {
                    val navController = rememberSwipeDismissableNavController()
                    SwipeDismissableNavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(route = homeRoot) {
                            HomeScreen(this@MainActivity) {
                                navController.navigate(walkRoot)
                            }
                        }
                        composable(route = walkRoot) {
                            WalkScreen(this@MainActivity) {
                                navController.navigate(homeRoot)
                            }
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
}