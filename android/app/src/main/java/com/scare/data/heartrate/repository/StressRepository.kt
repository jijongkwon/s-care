package com.scare.data.heartrate.repository

import com.scare.data.RetrofitClient
import com.scare.data.heartrate.api.DailyStressAPI
import com.scare.data.heartrate.api.request.DailyStressRequest
import com.scare.data.heartrate.api.response.ResponseResult
import retrofit2.Response

class StressRepository {

    val dailyStressAPI: DailyStressAPI by lazy {
        RetrofitClient.retrofit.create(DailyStressAPI::class.java)
    }

    //그날의 스트레스 지수 만들기
    suspend fun getDailyStress(createDailyStressReq: DailyStressRequest): Response<ResponseResult<Unit>> {
        return dailyStressAPI.createDailyStress(createDailyStressReq)
    }
}