package com.scare.service.listener

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class AuthRequestService(context: Context) {
    private val dataClient: DataClient = Wearable.getDataClient(context)

    fun sendAuthRequest() {
        val authDataReq = PutDataMapRequest.create("/authRequest").apply{
            dataMap.putString("String", "GIVE ME LOGIN")
            dataMap.putLong("timestamp", System.currentTimeMillis()) // 매번 다른 데이터로 인식되도록

        }.asPutDataRequest().setUrgent()

        dataClient.putDataItem(authDataReq).addOnSuccessListener {
            Log.d("AuthRequestService", "Login status request sent to mobile")
        }.addOnFailureListener { e ->
            Log.e("AuthRequestService", "Failed to send login status request", e)
        }
    }
}