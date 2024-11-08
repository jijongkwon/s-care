package com.scare.ui.mobile.viewmodel.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.data.member.dto.User.UserInfoResponseDTO
import com.scare.data.RetrofitClient.apiService
import com.scare.data.member.repository.Auth.TokenRepository
import com.scare.data.member.repository.User.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoViewModel(
    private val userRepository: UserInfoRepository,
    private val tokenRepository: TokenRepository) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfoResponseDTO?>(null)
    val userInfo: StateFlow<UserInfoResponseDTO?> = _userInfo

    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                val userInfo = userRepository.getUserInfo()

                if (userInfo != null) {
                    _userInfo.value = userInfo
                } else {
                    Log.e("UserInfoViewModel", "userInfo = null")
                }
            } catch (e: Exception) {
                Log.e("UserInfoViewModel", "Error fetching user info", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                apiService.logout().enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            // 로그아웃 성공 처리
                            Log.d("UserInfoViewModel", "logout success")
                            viewModelScope.launch {
                                tokenRepository.clearToken("all")

                                val isAccessTokenCleared = tokenRepository.getAccessToken() == null
                                val isProfileUrlCleared = tokenRepository.profileUrlFlow.first() == null
                                Log.d("UserInfoViewModel", "access_token cleared: $isAccessTokenCleared")
                                Log.d("UserInfoViewModel", "profile_url cleared: $isProfileUrlCleared")
                            }
                        } else {
                            // 실패 처리
                            Log.d("UserInfoViewModel", "logout fail")
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        // 네트워크 오류 처리
                        Log.d("UserInfoViewModel", "network: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("UserInfoViewModel", "Error logout: ${e.message}")
            }
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            try {
                val memberId = userRepository.getMemberId()

                apiService.withdraw(memberId).enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            // 탈퇴 성공 처리
                            Log.d("UserInfoViewModel", "탈퇴 성공")
                            viewModelScope.launch {
                                tokenRepository.clearToken("all") // accessToken만 삭제
                            }
                        } else {
                            // 실패 처리
                            Log.d("UserInfoViewModel", "탈퇴 실패")
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.d("UserInfoViewModel", "네트워크 오류: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("UserInfoViewModel", "Error withdraw", e)
            }
        }
    }
}