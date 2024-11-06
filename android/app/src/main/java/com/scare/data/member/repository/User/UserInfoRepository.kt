package com.scare.data.member.repository.User

import android.util.Log
import com.scare.data.member.dto.User.UserInfoResponseDTO
import com.scare.data.member.network.RetrofitClient.apiService
import com.scare.data.member.network.RetrofitClient.tokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import android.util.Base64

class UserInfoRepository {
    suspend fun getUserInfo(): UserInfoResponseDTO? {

        val memberId = getMemberIdFromToken()

        // Retrofit API 호출하여 회원 정보 조회
        return withContext(Dispatchers.IO) {
            try {
                apiService.getUserInfo(memberId).data
            } catch (e: Exception) {
                // 예외 처리 (API 호출 중 오류 발생 시)
                e.printStackTrace()
                null
            }
        }
    }

    // memberId를 외부에서 가져올 수 있도록 public 함수로 제공
    suspend fun getMemberId(): Long {
        return getMemberIdFromToken()
    }

    private suspend fun getMemberIdFromToken(): Long {
        // tokenRepository에서 accessToken 가져오기
        val token = tokenRepository.getAccessToken() ?: return 0

        // 토큰에서 memberId 추출
        val memberId = decodeToken(token)?.getLong("memberId") ?: return 0

        return memberId
    }

    // JWT 토큰을 디코딩하여 payload에서 데이터를 추출하는 함수
    private fun decodeToken(token: String): JSONObject? {
        return try {
            val tokenParts = token.split(".")
            val payload = tokenParts[1] // 토큰의 두 번째 부분이 payload
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes)

            JSONObject(decodedString) // JSON 객체로 변환
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}