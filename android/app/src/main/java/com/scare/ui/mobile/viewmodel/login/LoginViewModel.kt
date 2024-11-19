package com.scare.ui.mobile.viewmodel.login

import GoogleLoginRepository
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.scare.data.member.dto.Auth.LoginRequestDTO
import com.scare.data.RetrofitClient
import com.scare.data.member.repository.Auth.TokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class LoginViewModel(
    private val googleLoginRepository: GoogleLoginRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _profileUrl = MutableStateFlow<String?>(null)
    val profileUrl: StateFlow<String?> get() = _profileUrl

    init {
        // DataStore에서 profileUrl을 불러오는 로직 추가
        viewModelScope.launch {
            tokenRepository.profileUrlFlow.collect { url ->
                _profileUrl.value = url
            }
        }
    }

    // Google Login Intent 제공
    fun getSignInIntent(): Intent {
        return googleLoginRepository.getSignInIntent()
    }

     // 로그인 결과 처리
    fun handleSignInResult(data: Intent?) {
        googleLoginRepository.handleSignInResult(data) { account: GoogleSignInAccount? ->
            if (account != null) {
                _profileUrl.value = account.photoUrl?.toString()

                val email = account.email ?: ""
                val nickname = account.displayName ?: ""
                val profileUrl = account.photoUrl?.toString() ?: ""

                Log.d("LoginViewModel", "Email: $email, Nickname: $nickname, Profile URL: $profileUrl")

                // 프로필사진 저장
                viewModelScope.launch {
                    tokenRepository.saveProfileUrl(profileUrl)
                }

                // LoginRequestDTO 객체 생성 후 서버로 전송
                val loginRequestDTO = LoginRequestDTO(email, nickname, profileUrl)

                viewModelScope.launch { // viewModelScope 내에서 호출
                    sendUserAPI(loginRequestDTO)
                }
            } else {
                // account가 null일 경우 로그 출력
                Log.e("LoginViewModel", "GoogleSignInAccount is null")
            }
        }
    }

    //유저 정보 보내는 api
    private suspend fun sendUserAPI(loginRequestDTO: LoginRequestDTO) {
        try {
            val response = RetrofitClient.apiService.login(loginRequestDTO).awaitResponse() // awaitResponse 사용

            if (response.isSuccessful) {
                // Header에서 Access Token 꺼내오기
                val accessToken = response.headers()["Authorization"] ?: ""

                Log.d("LoginViewModel", "Access Token: $accessToken")

                // Token 저장 로직 (DataStore 등에 저장)
                tokenRepository.saveAccessToken(accessToken) // viewModelScope 필요 없음
            } else {
                Log.e("LoginViewModel", "응답 실패: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "API 호출 실패: ${e.message}")
        }
    }
}
