package com.scare.data.heartrate.api

import com.scare.data.heartrate.api.request.DailyStressRequest
import com.scare.data.heartrate.api.response.ResponseResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DailyStressAPI {

    @POST("/api/v1/stress/daily")
    suspend fun createDailyStress(@Body dailyStressReq: DailyStressRequest): Response<ResponseResult<Unit>>

}