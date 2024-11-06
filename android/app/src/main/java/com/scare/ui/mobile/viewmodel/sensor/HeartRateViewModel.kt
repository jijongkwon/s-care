package com.scare.ui.mobile.viewmodel.sensor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeartRateViewModel: ViewModel() {
    private var _hrValue = MutableStateFlow(0.0)
    val hrValue: StateFlow<Double> get() = _hrValue

    fun updateHeartRate(hrValue: Double) {
        _hrValue.value = hrValue
    }
}