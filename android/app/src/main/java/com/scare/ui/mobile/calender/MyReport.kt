package com.scare.ui.mobile.calender

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.common.TheHeader

@Composable
fun MyReport() {
    val navController = LocalNavController.current

    Scaffold (
        topBar = { TheHeader(null, isMainPage = false) }
    ) { innerPadding ->
        Text("여기가 주간리포트", modifier = Modifier.padding(innerPadding))
    }
}