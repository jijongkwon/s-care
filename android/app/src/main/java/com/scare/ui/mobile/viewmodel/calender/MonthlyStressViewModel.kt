package com.scare.ui.mobile.viewmodel.calender

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.data.calender.repository.MonthlyStressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MonthlyStressViewModel(
    private val repository: MonthlyStressRepository
) : ViewModel() {

    private val _stressLevelMap = MutableStateFlow<Map<LocalDate, Int>>(emptyMap())
    val stressLevelMap: StateFlow<Map<LocalDate, Int>> = _stressLevelMap

    fun fetchMonthlyStressData(from: String, to: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMonthlyStress(from, to)
                if (response.isSuccessful) {

                    Log.d("MonthlyStressViewModel", "API success: ${response.body()}")

                    response.body()?.let { data ->
                        if (data.isSuccess) {
                            val stressMap = data.data.associate { response ->
                                LocalDate.parse(response.recordedAt, DateTimeFormatter.ISO_DATE) to response.stress
                            }

                            _stressLevelMap.value = stressMap
                            Log.d("MonthlyStressViewModel", "Updated stressLevelMap: $stressMap")
                        }
                    }
                } else {
                    Log.d("MonthlyStressViewModel", "API fail: ${response}")
                }
            } catch (e: Exception) {
                // 예외 처리
                Log.e("MonthlyStressViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}