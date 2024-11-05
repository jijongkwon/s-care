package com.scare.ui.mobile.viewmodel.user

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
            _userInfo.value = repository.getUserInfo() // Repository에서 데이터 가져오기
        }
    }
}