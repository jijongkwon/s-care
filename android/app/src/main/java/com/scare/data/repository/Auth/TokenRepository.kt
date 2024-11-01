package com.scare.data.repository.Auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.scare.data.datastore.authDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


object TokenRepository {

    private var dataStore: DataStore<Preferences>? = null

    // TokenRepository 초기화 함수
    fun init(context: Context) {
        if (dataStore == null) {
            dataStore = context.authDataStore
        }
    }

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    /*
        dataStore에 저장해야 token이 있는지 없는지를 검사, 자동로그인이 가능함
        dataStore는 비동기적으로 처리되어야 함
        api는 동기적으로 처리되어야 함 (HTTP)
        그래서 두개 다 구현 후 가져다 쓰는 걸로 했습니다

     */

    // 비동기로 AccessToken 가져오기
    val accessTokenFlow: Flow<String?> = dataStore?.data?.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    } ?: flowOf(null)

    // 동기적으로 AccessToken 가져오기
    fun getAccessToken(): String? = runBlocking {
        accessTokenFlow.first()
    }

    // AccessToken 저장
    suspend fun saveAccessToken(token: String) {
        dataStore?.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    // 비동기로 RefreshToken 가져오기
    val refreshTokenFlow: Flow<String?> = dataStore?.data?.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    } ?: flowOf(null)

    // 동기적으로 RefreshToken 가져오기
    fun getRefreshToken(): String? = runBlocking {
        refreshTokenFlow.first()
    }

    // RefreshToken 저장
    suspend fun saveRefreshToken(token: String) {
        dataStore?.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }

    // 모든 토큰 삭제
    suspend fun clearTokens() {
        dataStore?.edit { preferences ->
            preferences.clear()
        }
    }
}