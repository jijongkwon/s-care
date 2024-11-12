package com.scare.presentation.sensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeartRateViewModel : ViewModel() {

    private var _stress = MutableStateFlow(0)
    val stress: StateFlow<Int> = _stress

    fun updateStress(stress: Int) {
        _stress.value = stress
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
