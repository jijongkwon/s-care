package com.scare.presentation.sensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeartRateViewModel : ViewModel() {

    private var _hrValue = MutableStateFlow(0.0)
    val hrValue: StateFlow<Double> get() = _hrValue

    fun updateHeartRate(hrValue: Double) {
        _hrValue.value = hrValue
    }
}

class HeartRateViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeartRateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HeartRateViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
