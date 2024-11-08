package com.scare.viewmodel.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.datastore.IS_LOGGED_IN_KEY
import com.scare.datastore.authStore
import com.scare.datastore.saveLoginStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: Flow<Boolean> = _isLoggedIn

    init {
        // DataStore에서 로그인 상태를 관찰
        viewModelScope.launch {
            context.authStore.data.map { preferences ->
                preferences[IS_LOGGED_IN_KEY] ?: false
            }.collect { isLoggedInStatus ->
                _isLoggedIn.value = isLoggedInStatus
            }
        }
    }

    // 로그인 상태 저장
    fun updateLoginStatus(context: Context, isLoggedIn: Boolean) {
        saveLoginStatus(context, isLoggedIn)
        _isLoggedIn.value = isLoggedIn
    }
}