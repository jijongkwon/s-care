package com.scare.service.listener

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.datastore.IS_LOGGED_IN_KEY
import com.scare.datastore.authStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthStatusListenerService: WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/authStatus") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val isLoggedIn = dataMap.getBoolean("isLoggedIn")

                // SharedPreferences나 DataStore에 저장
                saveLoginStatus(this, isLoggedIn)
                Log.d("AuthStatusListenerService", "Received login status: $isLoggedIn ")
            }
        }
    }

    private fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        // DataStore에 로그인 상태를 비동기적으로 저장
        CoroutineScope(Dispatchers.IO).launch {
            context.authStore.edit { preferences ->
                preferences[IS_LOGGED_IN_KEY] = isLoggedIn
            }
        }
    }
}