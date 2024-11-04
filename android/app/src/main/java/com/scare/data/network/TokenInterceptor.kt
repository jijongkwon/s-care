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
import kotlin.text.substringAfter

class TokenInterceptor(
    private val tokenRepository: TokenRepository,
    private val apiService: () -> ApiService // 새 토큰을 요청하는 API 인터페이스
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = runBlocking { tokenRepository.getAccessToken() } //동기처리

        request = request.newBuilder()
            .addHeader("Authorization", "$accessToken")
            .build()

        val response = chain.proceed(request)

        // accessToken이 만료되어 401이 반환되면 새 토큰 요청
        if (response.code == 401) {
            response.close()

            // CoroutineScope를 사용하여 비동기적으로 새 토큰 발급 요청
            val newAccessToken = runBlocking {
                withContext(Dispatchers.IO) {
                    reissueAccessToken(chain)
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

    private suspend fun reissueAccessToken(chain: Interceptor.Chain): String? {
        // 헤더에서 accessToken 가져오기
        val accessToken = chain.request().header("Authorization")?.substringAfter("Bearer ")

        // 쿠키에서 refreshToken 가져오기
        val refreshToken = getRefreshTokenFromCookies(chain)

        if (refreshToken == null) {
            return null // 쿠키에서 refreshToken을 찾지 못하면 null 반환
        }

        // 새 토큰 요청을 위한 DTO 생성
        val refreshTokenRequest = RefreshRequestDTO(refreshToken)

        return try {
            val apiService = apiService() // ApiService 가져오기
            val call: Call<Unit> = apiService.refreshToken(refreshTokenRequest)
            val response = call.awaitResponse()

            if (response.isSuccessful) {
                // 응답 헤더에서 accessToken 추출
                response.headers()["Authorization"]
            } else {
                null // 실패 시 null 반환
            }
        } catch (e: Exception) {
            null // 네트워크 오류 등 예외 처리
        }
    }

    // 쿠키에서 refreshToken을 가져오는 함수
    private fun getRefreshTokenFromCookies(chain: Interceptor.Chain): String? {
        val cookies = chain.request().header("Cookie")

        // 쿠키 값이 존재하지 않으면 null 반환
        if (cookies.isNullOrEmpty()) return null

        // 쿠키에서 refreshToken 추출
        cookies.split(";").forEach { cookie ->
            val parts = cookie.split("=")
            if (parts.size == 2 && parts[0].trim() == "refreshToken") {
                return parts[1].trim() // refreshToken 반환
            }
        }

        return null
    }
}
