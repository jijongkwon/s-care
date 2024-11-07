package com.scare.service.listener

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class AuthRequestService(context: Context) {
    private val dataClient: DataClient = Wearable.getDataClient(context)

    fun sendAuthRequest() {
        val authDataReq = PutDataMapRequest.create("/authRequest").asPutDataRequest().setUrgent()

        dataClient.putDataItem(authDataReq).addOnSuccessListener {
            Log.d("AuthRequestService", "Login status request sent to mobile")
        }.addOnFailureListener { e ->
            Log.e("AuthRequestService", "Failed to send login status request", e)
        }
    }
}