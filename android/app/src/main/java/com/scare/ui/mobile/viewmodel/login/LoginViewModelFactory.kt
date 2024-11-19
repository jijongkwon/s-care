package com.scare.ui.mobile.viewmodel.login

import GoogleLoginRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scare.data.member.repository.Auth.TokenRepository

class LoginViewModelFactory(
    private val googleLoginRepository: GoogleLoginRepository,
    private val tokenRepository: TokenRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(googleLoginRepository, tokenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
