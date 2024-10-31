package com.scare.data.repository.Auth

import android.content.SharedPreferences

class TokenRepository(private val sharedPreferences: SharedPreferences) {

    fun getAccessToken(): String? = sharedPreferences.getString("access_token", null)

    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    fun getRefreshToken(): String? = sharedPreferences.getString("refresh_token", null)

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString("refresh_token", token).apply()
    }

    fun clearTokens() {
        TODO("Not yet implemented")
    }

//    fun refreshToken(): String? {
//        // 리프레시 토큰을 사용하여 새 액세스 토큰을 요청하는 로직을 추가합니다.
//        val newToken = // Retrofit을 통해 새 토큰 요청
//            newToken?.let { saveAccessToken(it) }
//        return newToken
//    }
}