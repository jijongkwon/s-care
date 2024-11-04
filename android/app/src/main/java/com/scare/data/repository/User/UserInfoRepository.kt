package com.scare.data.repository.User

import com.scare.data.dto.User.UserInfoResponseDTO
import com.scare.data.network.RetrofitClient.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserInfoRepository {
    suspend fun getUserInfo(): UserInfoResponseDTO? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getUserInfo() // API 호출
            } catch (e: Exception) {
                // 에러 처리 로그
                e.printStackTrace()
                null
            }
        }
    }
}