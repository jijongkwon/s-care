package com.scare.ui.mobile.viewmodel.sensor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeartRateViewModel: ViewModel() {
    private var _stress = MutableStateFlow(0)
    val stress: StateFlow<Int> get() = _stress

    fun updateStress(stress: Int) {
        _stress.value = stress
    }
}