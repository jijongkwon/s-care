package com.scare.service.listener

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService

class AuthStatusListenerService: WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/initialLoginResponse") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val isLoggedIn = dataMap.getBoolean("isLoggedIn")
                val userName = dataMap.getString("userName")

                // SharedPreferences나 DataStore에 저장
                saveLoginStatus(isLoggedIn, userName)
                Log.d("AuthStatusListenerService", "Received login status: $isLoggedIn for user: $userName")
            }
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean, userName: String?) {
        // SharedPreferences 또는 DataStore에 상태 저장 로직
    }
}