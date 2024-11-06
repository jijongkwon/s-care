package com.scare.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.scare.presentation.sensor.HeartRateViewModel
import com.scare.presentation.sensor.HeartRateViewModelFactory
import com.scare.presentation.theme.ScareTheme

@Composable
fun HomeApp() {
    ScareTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            timeText = { TimeText() }
        ) {
            val viewModel: HeartRateViewModel = viewModel(
                factory = HeartRateViewModelFactory()
            )
            val hrValue by viewModel.hrValue.collectAsState()

            HomeScreen(
                hrValue = hrValue,
            )
        }
    }
}