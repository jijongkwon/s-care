package com.scare.data.network

import com.scare.data.repository.Auth.TokenRepository

//class AuthInterceptor(private val tokenRepository: TokenRepository) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request()
//        val token = tokenRepository.getAccessToken()
//
//        request = request.newBuilder()
//            .addHeader("Authorization", "Bearer $token")
//            .build()
//
//        val response = chain.proceed(request)
//
//        // 토큰 만료 시 리프레시 토큰을 사용해 새 토큰 요청
//        if (response.code == 401) {
//            synchronized(this) {
//                val newToken = tokenRepository.refreshToken() // 리프레시 토큰으로 새 토큰 요청
//                if (newToken != null) {
//                    tokenRepository.saveAccessToken(newToken)
//                    request = request.newBuilder()
//                        .header("Authorization", "Bearer $newToken")
//                        .build()
//                    return chain.proceed(request)
//                }
//            }
//        }
//        return response
//    }
//}