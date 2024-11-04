package com.scare.ui.mobile.viewmodel.login

import GoogleLoginRepository
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.scare.data.dto.Auth.LoginRequestDTO
import com.scare.data.dto.Auth.LoginResponseDTO
import com.scare.data.network.RetrofitClient
import com.scare.data.repository.Auth.TokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val googleLoginRepository: GoogleLoginRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _profileUrl = MutableStateFlow<String?>(null)
    val profileUrl: StateFlow<String?> get() = _profileUrl

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

                // 서버로 사용자 정보 전송
//                sendUserAPI(email, nickname, profileUrl)

                // 실제 서버에서 토큰을 받아 저장하는 로직을 구현
                val accessToken = "서버에서 받은 accessToken" // 실제 서버에서 받은 값으로 대체
                val refreshToken = "서버에서 받은 refreshToken" // 실제 서버에서 받은 값으로 대체

                // suspend 함수를 viewModelScope 내에서 호출
                viewModelScope.launch {
                    tokenRepository.saveAccessToken(accessToken)
                    tokenRepository.saveRefreshToken(refreshToken)
                }
            } else {
                // account가 null일 경우 로그 출력
                println("GoogleSignInAccount is null")
            }
        }
    }

    //유저 정보 보내는 api
    private fun sendUserAPI(email: String, nickname: String, profileUrl: String) {
        // LoginRequestDTO 객체 생성
        val loginRequestDTO = LoginRequestDTO(email, nickname, profileUrl)

        val call = RetrofitClient.apiService.login(loginRequestDTO)
        call.enqueue(object : Callback<LoginResponseDTO> {
            override fun onResponse(call: Call<LoginResponseDTO>, response: Response<LoginResponseDTO>) {

                Log.d("Reponse", response.toString())

//                if (response.isSuccessful) {
//                    //여기에서 Header에서 꺼내오기
//
//
//                    val accessToken = response.body()?.accessToken ?: ""
//                    val refreshToken = response.body()?.refreshToken ?: ""
//
//                    // suspend 함수를 viewModelScope 내에서 호출
//                    viewModelScope.launch {
//                        tokenRepository.saveAccessToken(accessToken)
//                        tokenRepository.saveRefreshToken(refreshToken)
//                    }
//                }
            }

            override fun onFailure(call: Call<LoginResponseDTO>, t: Throwable) {
                // 실패 처리
            }
        })
    }

    // 로그아웃 처리
    fun signOut() {
        googleLoginRepository.signOut()
        _profileUrl.value = null

        viewModelScope.launch {
            tokenRepository.clearTokens()
        }
    }
}
