package com.scare.ui.mobile.viewmodel.calender

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.data.calender.dto.WeeklyReportResponseDTO
import com.scare.data.calender.repository.WeeklyReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeeklyReportViewModel(
    private val repository: WeeklyReportRepository
) : ViewModel() {

    private val _weeklyReport = MutableStateFlow<WeeklyReportResponseDTO?>(null)
    val weeklyReport: StateFlow<WeeklyReportResponseDTO?> = _weeklyReport

    fun fetchWeeklyReport(from: String, to: String) {
        viewModelScope.launch {
            val response = repository.getWeeklyReport(from, to)

            if (response.isSuccessful) {
                _weeklyReport.value = response.body()
            } else {
                Log.d("WeeklyReportViewModel", "API fail: ${response}")
            }
        }
    }

}