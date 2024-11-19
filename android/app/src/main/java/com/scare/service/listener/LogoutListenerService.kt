package com.scare.service.listener

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class LogoutListenerService(context: Context) {

    private val dataClient: DataClient = Wearable.getDataClient(context)

    fun sendAuthRequest(isLoggedIn: Boolean) {
        val authDataReq = PutDataMapRequest.create("/logoutState").apply {
            dataMap.putBoolean("isLoggedIn", isLoggedIn)
        }.asPutDataRequest().setUrgent()


        dataClient.putDataItem(authDataReq).addOnSuccessListener {
            Log.d("LogoutListenerService", "Logout")
        }.addOnFailureListener { e ->
            Log.e("LogoutListenerService", "Failed to send logout request", e)
        }
    }
}