package com.scare.ui.mobile.viewmodel.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scare.data.calender.repository.WeeklyReportRepository

class WeeklyReportViewModelFactory(
    private val repository: WeeklyReportRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeeklyReportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}