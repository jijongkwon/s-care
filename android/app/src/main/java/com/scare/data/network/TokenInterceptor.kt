package com.scare.data.network

import com.scare.data.dto.Auth.LoginResponseDTO
import com.scare.data.dto.Auth.RefreshRequestDTO
import com.scare.data.repository.Auth.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.awaitResponse

class TokenInterceptor(
    private val tokenRepository: TokenRepository,
    private val apiService: ApiService // 새 토큰을 요청하는 API 인터페이스
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = runBlocking { tokenRepository.getAccessToken() } //동기처리

        request = request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(request)

        // accessToken이 만료되어 401이 반환되면 새 토큰 요청
        if (response.code == 401) {
            response.close()

            // CoroutineScope를 사용하여 비동기적으로 새 토큰 발급 요청
            val newAccessToken = runBlocking {
                withContext(Dispatchers.IO) {
                    reissueAccessToken()
                }
            }

            if (newAccessToken != null) {
                runBlocking { tokenRepository.saveAccessToken(newAccessToken) }

                request = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()

                return chain.proceed(request)
            }
        }

        return response
    }

    private suspend fun reissueAccessToken(): String? {
        val refreshToken = tokenRepository.getRefreshToken()

        // 새 토큰 요청을 위한 refreshToken을 DTO로 래핑
        val refreshTokenRequest = RefreshRequestDTO(refreshToken ?: "")

        return try {
            val call: Call<LoginResponseDTO> = apiService.refreshToken(refreshTokenRequest)
            val response = call.awaitResponse() // suspend 함수로 대기

            if (response.isSuccessful) {
                response.body()?.accessToken // 성공 시 accessToken 반환
            } else {
                null // 실패 시 null 반환
            }
        } catch (e: Exception) {
            null // 네트워크 오류 등 예외 처리
        }
    }
}
