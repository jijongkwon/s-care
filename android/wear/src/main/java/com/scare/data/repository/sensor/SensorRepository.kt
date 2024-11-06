package com.scare.data.repository.sensor

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.scare.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "heart_rate")

class SensorRepository(private val context: Context) {
    val passiveDataEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PASSIVE_DATA_ENABLED] ?: true
    }
    private val dataClient by lazy { Wearable.getDataClient(context) }

    suspend fun setPassiveDataEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PASSIVE_DATA_ENABLED] = enabled
        }
    }

    val latestHeartRate: Flow<Double> = context.dataStore.data.map { prefs ->
        prefs[LATEST_HEART_RATE] ?: 0.0
    }

    suspend fun storeLatestHeartRate(hrValue: Double) {
        Log.d(TAG, "sensor: $hrValue")
        sendToHandheldDevice(hrValue)
        context.dataStore.edit { prefs ->
            prefs[LATEST_HEART_RATE] = hrValue
        }
    }

    private fun sendToHandheldDevice(hrValue: Double) {
        val putDataReq = PutDataMapRequest.create("/heartRate").apply {
            dataMap.putDouble("hrValue", hrValue)
            dataMap.putLong("timestamp", System.currentTimeMillis())  // Optional: add a timestamp
        }.asPutDataRequest()
            .setUrgent()

        dataClient.putDataItem(putDataReq).addOnSuccessListener {
            Log.d(TAG, "HrValue is sent to handheld device successfully")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to send hrValue to handheld device", e)
        }
    }

    companion object {
        private val PASSIVE_DATA_ENABLED = booleanPreferencesKey("passive_data_enabled")
        private val LATEST_HEART_RATE = doublePreferencesKey("latest_heart_rate")
    }
}