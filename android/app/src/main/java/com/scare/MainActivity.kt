package com.scare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.scare.ui.mobile.calender.MyCalender
import com.scare.ui.mobile.course.MyCourse
import com.scare.ui.mobile.main.MainPage
import com.scare.ui.mobile.main.StartPage
import com.scare.ui.mobile.map.Map
import com.scare.ui.theme.ScareTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalNaverMapApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScareTheme {
                val navController = androidx.navigation.compose.rememberNavController()

                NavHost(navController = navController, startDestination = "main") {
                    composable("start") { StartPage(navController) }
                    composable("main",
                        arguments = listOf(
                            navArgument("imageUrl") { type = NavType.StringType },
                            navArgument("accessToken") { type = NavType.StringType }
                        )) { backStackEntry ->
                        val imageUrl = backStackEntry.arguments?.getString("imageUrl")
                        val accessToken = backStackEntry.arguments?.getString("accessToken")
                        MainPage(imageUrl, accessToken, navController)
                    }
                    composable("statistics") { MyCalender() } // "statistics" 경로 추가
                    composable("walk") { MyCourse() } // "walk" 경로 추가
                    composable("map") { Map() } // "map" 경로 추가
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScarePreview() {
    ScareTheme {
        StartPage(navController = rememberNavController())
    }
}