package com.scare.data.walk.network

import com.scare.data.walk.dto.WalkRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface WalkApi {
    @POST("/api/v1/walking")
    suspend fun postWalkingCourse(@Body walkRequestDTO: WalkRequestDTO)
}
