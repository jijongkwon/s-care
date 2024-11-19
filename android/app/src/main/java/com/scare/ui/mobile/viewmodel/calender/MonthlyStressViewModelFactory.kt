package com.scare.ui.mobile.viewmodel.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scare.data.calender.repository.MonthlyStressRepository

class MonthlyStressViewModelFactory(
    private val repository: MonthlyStressRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyStressViewModel::class.java)) {
            return MonthlyStressViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}