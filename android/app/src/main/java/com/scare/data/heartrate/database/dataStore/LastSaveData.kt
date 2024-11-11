package com.scare.data.heartrate.database.dataStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val Context.dataStore by preferencesDataStore("user_heartrate")

class LastSaveData(private val context: Context) {

    companion object {
        val LAST_SAVE_DATE_KEY = stringPreferencesKey("last_save_date")
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    }

    // lastSaveDate 가져오기
    val lastSaveDate: Flow<LocalDateTime?> = context.dataStore.data.map { preferences ->
        preferences[LAST_SAVE_DATE_KEY]?.let { LocalDateTime.parse(it, formatter) }
    }

    // lastSaveDate 업데이트
    suspend fun updateLastSaveDate(date: LocalDateTime) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SAVE_DATE_KEY] = date.format(formatter)
        }
    }

}