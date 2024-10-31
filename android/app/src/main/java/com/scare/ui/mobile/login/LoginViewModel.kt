package com.scare.ui.mobile.login

import GoogleLoginRepository
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.scare.data.repository.Auth.TokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
                // 실제 서버에서 토큰을 받아 저장하는 로직을 구현
                val accessToken = "서버에서 받은 accessToken" // 실제 서버에서 받은 값으로 대체
                val refreshToken = "서버에서 받은 refreshToken" // 실제 서버에서 받은 값으로 대체
                tokenRepository.saveAccessToken(accessToken)
                tokenRepository.saveRefreshToken(refreshToken)
            } else {
                // account가 null일 경우 로그 출력
                println("GoogleSignInAccount is null")
            }
        }
    }

    // 로그아웃 처리
    fun signOut() {
        googleLoginRepository.signOut()
        _profileUrl.value = null
        tokenRepository.clearTokens()
    }
}
