package com.scare.wear.presentation.sensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.scare.wear.data.repository.sensor.HealthServicesRepository
import com.scare.wear.data.repository.sensor.SensorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SensorViewModel(
    private val healthServicesRepository: HealthServicesRepository,
    private val sensorRepository: SensorRepository
) : ViewModel() {
    val hrValue = sensorRepository.latestHeartRate
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Double.NaN)

    init {
        viewModelScope.launch {
            sensorRepository.passiveDataEnabled.distinctUntilChanged().collect { enabled ->
                if (enabled) {
                    healthServicesRepository.registerForHeartRateData()
                } else {
                    healthServicesRepository.unregisterForHeartRateData()
                }
            }
        }
    }
}

class SensorViewModelFactory(
    private val healthServicesRepository: HealthServicesRepository,
    private val sensorRepository: SensorRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SensorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SensorViewModel(
                healthServicesRepository = healthServicesRepository,
                sensorRepository = sensorRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
