package com.scare.data.calender.api

import com.scare.data.calender.dto.WeeklyReportResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeeklyResportAPI {

    @GET("/api/v1/report/weekly")
    suspend fun getWeeklyReport(
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<WeeklyReportResponseDTO>

}