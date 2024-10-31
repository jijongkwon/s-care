package com.scare.ui.mobile.login

import GoogleLoginRepository
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class LoginViewModel(private val googleLoginRepository: GoogleLoginRepository) : ViewModel() {
    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> = _email

    private val _nickName = MutableLiveData<String?>()
    val nickName: LiveData<String?> = _nickName

    private val _profileUrl = MutableLiveData<String?>()
    val profileUrl: LiveData<String?> = _profileUrl

    // Google 로그인 Intent 반환
    fun getSignInIntent(): Intent = googleLoginRepository.getSignInIntent()

    // 로그인 결과 처리
    fun loginResult(data: Intent?) {
        googleLoginRepository.handleSignInResult(data) { account: GoogleSignInAccount? ->
            _email.value = account?.email
            _nickName.value = account?.displayName
            _profileUrl.value = account?.photoUrl?.toString()
        }
    }

    // 로그아웃 처리
    fun signOut() {
        googleLoginRepository.signOut()
        _email.value = null
        _nickName.value = null
        _profileUrl.value = null
    }
}