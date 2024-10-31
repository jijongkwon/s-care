package com.scare

import android.content.Intent
import android.os.Bundle
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
import com.scare.ui.mobile.calender.MyCalender
import com.scare.ui.mobile.course.MyCourse
import com.scare.ui.mobile.login.LoginActivity
import com.scare.ui.mobile.login.LoginViewModel

import com.scare.ui.mobile.main.MainPage
import com.scare.ui.mobile.main.StartPage
import com.scare.ui.theme.ScareTheme

class MainActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            loginViewModel.handleSignInResult(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setContent {
            ScareTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "start") {
                    composable("start") { StartPage(navController, loginViewModel) { launchLogin() } }
                    composable("main") { MainPage(loginViewModel, navController) } // loginViewModel 전달
                    composable("statistics") { MyCalender() }
                    composable("walk") { MyCourse() }
                }
            }
        }
    }

    private fun launchLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        signInLauncher.launch(loginIntent)
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScarePreview() {
    ScareTheme {
        StartPage(navController = rememberNavController())
    }
}