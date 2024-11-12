package com.scare.data.walk.repository

import com.scare.data.RetrofitClient
import com.scare.data.walk.dto.WalkRequestDTO
import com.scare.data.walk.network.WalkApi

class WalkRepository {

    val walkApi: WalkApi by lazy {
        RetrofitClient.retrofit.create(WalkApi::class.java)
    }

    suspend fun postWalk(walk: WalkRequestDTO) {
        return walkApi.postWalkingCourse(walk)
    }
    
}