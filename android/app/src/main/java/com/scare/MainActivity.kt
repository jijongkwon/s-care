package com.scare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Configuration

import com.scare.ui.mobile.main.MainPage
import com.scare.ui.mobile.main.StartPage
import com.scare.ui.theme.ScareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScareTheme {
                val navController = androidx.navigation.compose.rememberNavController()

                NavHost(navController = navController, startDestination = "start") {
                    composable("start") { StartPage(navController) }
                    composable("main") { MainPage() } // 다른 화면을 위한 Composable 함수
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScarePreview() {
    ScareTheme {
//        StartPage(rememberNavController())
        MainPage()
    }
}