package com.scare.wear.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.scare.wear.data.repository.sensor.HealthServicesRepository
import com.scare.wear.data.repository.sensor.SensorRepository
import com.scare.wear.presentation.sensor.SensorViewModel
import com.scare.wear.presentation.sensor.SensorViewModelFactory
import com.scare.wear.presentation.theme.ScareTheme

@Composable
fun HomeApp(
    healthServicesRepository: HealthServicesRepository,
    sensorRepository: SensorRepository
) {
    ScareTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            timeText = { TimeText() }
        ) {
            val viewModel: SensorViewModel = viewModel(
                factory = SensorViewModelFactory(
                    healthServicesRepository = healthServicesRepository,
                    sensorRepository = sensorRepository
                )
            )
            val hrValue by viewModel.hrValue.collectAsState()

            HomeScreen(
                hrValue = hrValue,
            )
        }
    }
}