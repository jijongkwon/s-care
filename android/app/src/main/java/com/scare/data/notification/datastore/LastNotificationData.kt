package com.scare.data.heartrate.notification.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val Context.dataStore by preferencesDataStore("user_notification")

class LastNotificationData(private val context: Context) {

    companion object {
        val LAST_NOTIFICATION_DATE_KEY = stringPreferencesKey("last_notification_date")
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    }

    val lastNotificationDate: Flow<LocalDateTime?> = context.dataStore.data.map { preferences ->
        preferences[LAST_NOTIFICATION_DATE_KEY]?.let { LocalDateTime.parse(it, formatter) }
    }

    suspend fun updateLastNotificationDate(date: LocalDateTime) {
        context.dataStore.edit { preferences ->
            preferences[LAST_NOTIFICATION_DATE_KEY] = date.format(formatter)
        }
    }

}