package com.scare.ui.mobile.viewmodel.walk

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scare.data.walk.repository.WalkRepository
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.repository.location.LocationRepository

class WalkViewModelFactory(
    private val context: Context,
    private val heartRateRepository: HeartRateRepository,
    private val locationRepository: LocationRepository,
    private val walkRepository: WalkRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalkViewModel::class.java)) {
            return WalkViewModel(context, heartRateRepository, locationRepository, walkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
