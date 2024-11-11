package com.scare.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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

// 산책 상태 저장 함수
fun saveWalkStatus(context: Context, isWalk: Boolean) {
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