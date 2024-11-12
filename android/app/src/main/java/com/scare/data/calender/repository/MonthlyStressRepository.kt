package com.scare.data.calender.repository

import com.scare.data.RetrofitClient
import com.scare.data.calender.api.MonthlyStressAPI
import com.scare.data.calender.dto.MonthlyStressResponseDTO
import com.scare.data.heartrate.api.DailyStressAPI
import com.scare.data.heartrate.api.response.ResponseResult
import retrofit2.Response

class MonthlyStressRepository {

    val monthlyStressAPI: MonthlyStressAPI by lazy {
        RetrofitClient.retrofit.create(MonthlyStressAPI::class.java)
    }

    suspend fun getMonthlyStress(from: String, to: String): Response<MonthlyStressResponseDTO> {
        return monthlyStressAPI.getMonthlyStress(from, to)
    }
}