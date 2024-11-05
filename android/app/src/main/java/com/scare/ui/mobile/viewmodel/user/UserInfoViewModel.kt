package com.scare.ui.mobile.viewmodel.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.data.member.dto.User.UserInfoResponseDTO
import com.scare.data.member.repository.User.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserInfoViewModel(private val repository: UserInfoRepository) : ViewModel() {
    private val _userInfo = MutableStateFlow<UserInfoResponseDTO?>(null)
    val userInfo: StateFlow<UserInfoResponseDTO?> = _userInfo

    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                val userInfo = repository.getUserInfo()

                if (userInfo != null) {
                    Log.d("UserInfoViewModel", "User info fetched: $userInfo")
                    _userInfo.value = userInfo
                } else {
                    Log.e("UserInfoViewModel", "userInfo = null")
                }
            } catch (e: Exception) {
                Log.e("UserInfoViewModel", "Error fetching user info", e)
            }
        }
    }
}