package com.scare.ui.mobile.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scare.data.member.repository.Auth.TokenRepository
import com.scare.data.member.repository.User.UserInfoRepository

class UserInfoViewModelFactory(
    private val repository: UserInfoRepository,
    private val tokenRepository: TokenRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserInfoViewModel(repository, tokenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
