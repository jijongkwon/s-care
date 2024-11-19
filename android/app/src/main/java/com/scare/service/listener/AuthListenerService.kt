package com.scare.service.listener

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.scare.data.member.repository.Auth.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthListenerService: WearableListenerService() {
    private lateinit var dataClient: DataClient

    override fun onCreate() {
        super.onCreate()
        dataClient = Wearable.getDataClient(this)
        Log.d("AuthListenerService", "Service created")
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("AuthListenerService", "COME?")

        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/authRequest") {

                // 로그인 상태 확인
                val isLoggedIn = checkLoginStatus(this) // 로그인 상태 확인 함수 호출

                Log.d("AuthListenerService", "$isLoggedIn")

                val putDataReq = PutDataMapRequest.create("/authStatus").apply {
                    dataMap.putBoolean("isLoggedIn", isLoggedIn)
                }.asPutDataRequest().setUrgent()

                // 응답 전송
                dataClient.putDataItem(putDataReq).addOnSuccessListener {
                    Log.d("AuthListenerService", "Login status response sent to watch")
                }.addOnFailureListener { e ->
                    Log.e("AuthListenerService", "Failed to send login status response", e)
                }
            }
        }
    }
}

fun checkLoginStatus(context: Context): Boolean {
    val tokenRepository = TokenRepository.getInstance(context) // Context 전달

    return runBlocking {
        val token = tokenRepository.getAccessToken()
        !token.isNullOrEmpty() // AccessToken이 존재하면 true 반환
    }
}