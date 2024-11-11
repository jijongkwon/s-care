package com.scare.ui.mobile.viewmodel.stress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.data.heartrate.api.request.CreateDailyStressReq
import com.scare.data.heartrate.api.request.DailyStressRequest
import com.scare.data.heartrate.database.dataStore.LastSaveData
import com.scare.data.heartrate.repository.StressRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class StressViewModel (
    private val lastSaveData: LastSaveData,
    private val stressStoreManager: StressStoreManager,
    private val stressRepository: StressRepository
): ViewModel() {

    fun uploadDailyStressData() {
        viewModelScope.launch {
            // DataStore에서 lastSaveDate 불러오기 (기본값으로 LocalDateTime.now().minusDays(1)을 사용)
            val todayDate = LocalDate.now()
            val lastSaveDate = lastSaveData.lastSaveDate.first()?.toLocalDate()

            // DataStore에 저장된 날짜가 오늘과 같으면 API 요청 생략
            if (lastSaveDate == todayDate) {
                Log.d("StressViewModel", "Already API Success")
                return@launch
            }

            // calculateDailyStressSince를 suspend 함수로 호출하여 비동기 실행
            val dailyStressRequests: DailyStressRequest = stressStoreManager.calculateDailyStressSince(
                lastSaveDate?.atStartOfDay() ?: LocalDateTime.of(todayDate.minusDays(1), LocalTime.MIN)
            )

            Log.d("StressViewModel", dailyStressRequests.toString())

            // 데이터가 없으면 API 요청 생략
            if (dailyStressRequests.dailyStressList.isEmpty()) {
                Log.d("StressViewModel", "No Data for API")
                return@launch
            }

            // 각 요청을 API에 전송
            val response = stressRepository.getDailyStress(dailyStressRequests)

            if (response.isSuccessful) {
                // API 요청 성공 시 lastSaveDate를 오늘 날짜로 업데이트
                lastSaveData.updateLastSaveDate(LocalDateTime.now())
                Log.d("StressViewModel", "API SUCCESS: LastSaveData Update")
            } else {
                Log.e("StressViewModel", "code: ${response.code()}, errorMessage: ${response.errorBody()?.string()}")
            }
        }
    }
}