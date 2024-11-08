package com.scare.data.member.repository.Auth

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.scare.data.member.datastore.authDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class TokenRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var instance: TokenRepository? = null

        // Thread-safe 초기화 함수
        fun getInstance(context: Context): TokenRepository {
            return instance ?: synchronized(this) {
                instance ?: TokenRepository(context.authDataStore).also {
                    instance = it
                }
            }
        }

        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val PROFILE_URL_KEY = stringPreferencesKey("profile_url")
    }

    // profileUrl 저장
    suspend fun saveProfileUrl(profileUrl: String) {
        dataStore.edit { preferences ->
            preferences[PROFILE_URL_KEY] = profileUrl
        }
    }

    // profileUrl 불러오기
    val profileUrlFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PROFILE_URL_KEY]
    }

    // 비동기로 AccessToken 가져오기
    val accessTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        val token = preferences[ACCESS_TOKEN_KEY]
        Log.d("TokenRepository", "accessTokenFlow updated: $token")
        token
    }

    // 비동기적으로 AccessToken 가져오기
    suspend fun getAccessToken(): String? {
        return accessTokenFlow.first() // 최신 값 반환
    }

    // AccessToken 저장
    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    // 특정 토큰 삭제
    suspend fun clearToken(tokenType: String) {
        dataStore.edit { preferences ->
            when (tokenType) {
                "access_token" -> preferences.remove(ACCESS_TOKEN_KEY)
                "profile_url" -> preferences.remove(PROFILE_URL_KEY)
                "all" -> preferences.clear() // 모든 데이터 삭제
            }
        }
    }
}
