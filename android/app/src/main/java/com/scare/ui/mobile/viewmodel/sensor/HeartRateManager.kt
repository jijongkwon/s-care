package com.scare.ui.mobile.viewmodel.sensor

object HeartRateManager {
    private lateinit var viewModel: HeartRateViewModel

    fun setViewModel(viewModel: HeartRateViewModel) {
        this.viewModel = viewModel
    }

    fun updateStress(stress: Int) {
        viewModel.updateStress(stress)
    }
}