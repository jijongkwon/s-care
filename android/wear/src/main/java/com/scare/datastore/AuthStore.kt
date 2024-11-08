package com.scare.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// DataStore 정의
val Context.authStore by preferencesDataStore("auth_prefs")

// Key 정의
val IS_LOGGED_IN_KEY = booleanPreferencesKey("isLoggedIn")

// 로그인 상태 저장 함수
fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
    Log.d("WearAuthDataStore", "${isLoggedIn}")
    CoroutineScope(Dispatchers.IO).launch {
        context.authStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }
}

// 로그인 상태 불러오기 함수
fun getLoginStatus(context: Context): Flow<Boolean> {
    return context.authStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }
}