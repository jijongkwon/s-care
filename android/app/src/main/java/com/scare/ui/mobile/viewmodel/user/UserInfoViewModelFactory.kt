package com.scare.ui.mobile.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scare.data.member.repository.User.UserInfoRepository

class UserInfoViewModelFactory(private val repository: UserInfoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
