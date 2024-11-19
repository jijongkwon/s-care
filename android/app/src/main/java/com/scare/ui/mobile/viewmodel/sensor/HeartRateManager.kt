package com.scare.ui.mobile.viewmodel.sensor

import android.util.Log

object HeartRateManager {
    private lateinit var viewModel: HeartRateViewModel

    fun setViewModel(viewModel: HeartRateViewModel) {
        this.viewModel = viewModel
    }

    fun updateStress(stress: Int) {
        if (!::viewModel.isInitialized) {
            Log.e("HeartRateManager", "HeartRateViewModel has not been initialized.")
            return
        }
        viewModel.updateStress(stress)
    }
}