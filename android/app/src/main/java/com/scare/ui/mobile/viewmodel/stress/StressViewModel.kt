package com.scare.ui.mobile.viewmodel.stress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.data.heartrate.api.request.CreateDailyStressReq
import com.scare.data.heartrate.database.dataStore.LastSaveData
import com.scare.data.heartrate.repository.StressRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StressViewModel (
    private val lastSaveData: LastSaveData,
    private val stressStoreManager: StressStoreManager,
    private val stressRepository: StressRepository
): ViewModel() {

    fun uploadDailyStressData() {
        viewModelScope.launch {
            // DataStore에서 lastSaveDate 불러오기 (기본값으로 LocalDateTime.now().minusDays(1)을 사용)
            val lastSaveDate = lastSaveData.lastSaveDate.first() ?: LocalDateTime.now().minusDays(1)

            // calculateDailyStressSince 함수 호출하여 요청 데이터 생성
            val dailyStressRequests: List<CreateDailyStressReq> = stressStoreManager.calculateDailyStressSince(lastSaveDate)

            // 각 요청을 API에 전송
            val response = stressRepository.getDailyStress(dailyStressRequests)

            if (response.isSuccessful) {
                // API 요청 성공 시 lastSaveDate를 오늘 날짜로 업데이트
                val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                lastSaveData.updateLastSaveDate(LocalDateTime.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            } else {
                Log.e("StressViewModel", "code: ${response.code()}, errorMessage: ${response.errorBody()?.string()}")
            }
        }
    }
}