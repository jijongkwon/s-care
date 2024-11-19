package com.scare.data.walk.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.scare.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// DataStore 정의
val Context.walkStore by preferencesDataStore("walk_prefs")

// Key 정의
val IS_WALK_KEY = booleanPreferencesKey("isWalk")
val WALK_START_TIME = stringPreferencesKey("walkStartTime")
val WALK_END_TIME = stringPreferencesKey("walkEndTime")

// 산책 상태 저장 함수
suspend fun saveWalkStatus(context: Context, isWalk: Boolean) {
    Log.d(TAG, "isWalk: $isWalk")
    CoroutineScope(Dispatchers.IO).launch {
        context.walkStore.edit { preferences ->
            preferences[IS_WALK_KEY] = isWalk
        }
    }
}

// 산책 상태 불러오기 함수
fun getWalkStatus(context: Context): Flow<Boolean> {
    return context.walkStore.data.map { preferences ->
        preferences[IS_WALK_KEY] ?: false
    }
}

// 산책 시작 시간 저장 함수
suspend fun saveWalkStartTime(context: Context, startTime: String) {
    Log.d(TAG, "isWalk: $startTime")
    CoroutineScope(Dispatchers.IO).launch {
        context.walkStore.edit { preferences ->
            preferences[WALK_START_TIME] = startTime
        }
    }
}

// 산책 시작 시간 불러오기 함수
fun getWalkStartTime(context: Context): Flow<String> {
    return context.walkStore.data.map { preferences ->
        preferences[WALK_START_TIME] ?: ""
    }
}

// 산책 완료 시간 저장 함수
suspend fun saveWalkEndTime(context: Context, endTime: String) {
    Log.d(TAG, "isWalk: $endTime")
    CoroutineScope(Dispatchers.IO).launch {
        context.walkStore.edit { preferences ->
            preferences[WALK_END_TIME] = endTime
        }
    }
}

// 산책 종료 시간 불러오기 함수
fun getWalkEndTime(context: Context): Flow<String> {
    return context.walkStore.data.map { preferences ->
        preferences[WALK_END_TIME] ?: ""
    }
}