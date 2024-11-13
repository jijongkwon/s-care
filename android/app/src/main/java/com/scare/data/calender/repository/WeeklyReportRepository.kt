package com.scare.data.calender.repository

import com.scare.data.RetrofitClient
import com.scare.data.calender.api.WeeklyResportAPI
import com.scare.data.calender.dto.WeeklyReportResponseDTO
import retrofit2.Response

class WeeklyReportRepository {

    val weeklyReportAPI: WeeklyResportAPI by lazy {
        RetrofitClient.retrofit.create(WeeklyResportAPI::class.java)
    }

    suspend fun getWeeklyReport(from: String, to: String): Response<WeeklyReportResponseDTO> {
        return weeklyReportAPI.getWeeklyReport(from, to)
    }

}