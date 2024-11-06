package com.scare.ui.mobile.viewmodel.sensor

object HeartRateManager {
    private lateinit var viewModel: HeartRateViewModel

    fun setViewModel(viewModel: HeartRateViewModel) {
        this.viewModel = viewModel
    }

    fun updateHeartRate(hrValue: Double) {
        viewModel.updateHeartRate(hrValue)
    }
}