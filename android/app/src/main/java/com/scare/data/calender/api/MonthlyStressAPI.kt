package com.scare.data.calender.api

import com.scare.data.calender.dto.MonthlyStressResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MonthlyStressAPI {

    @GET("/api/v1/stress/monthly")
    suspend fun getMonthlyStress(
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<MonthlyStressResponseDTO>

}