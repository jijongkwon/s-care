package com.scare.ui.mobile.viewmodel.walk

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.repository.location.LocationRepository

class WalkViewModelFactory(
    private val context: Context,
    private val heartRateRepository: HeartRateRepository,
    private val locationRepository: LocationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalkViewModel::class.java)) {
            return WalkViewModel(context, heartRateRepository, locationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
