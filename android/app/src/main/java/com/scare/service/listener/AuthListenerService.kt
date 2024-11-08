package com.scare.service.listener

import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.scare.data.member.repository.Auth.TokenRepository
import kotlinx.coroutines.runBlocking

class AuthListenerService: WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {

        // dataClient 초기화
        val dataClient = Wearable.getDataClient(this)

        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/authRequest") {

                // 로그인 상태 확인
                val isLoggedIn = checkLoginStatus() // 로그인 상태 확인 함수 호출

                Log.d("AuthListenerService", "$isLoggedIn")

                val putDataReq = PutDataMapRequest.create("/authStatus").apply {
                    dataMap.putBoolean("isLoggedIn", isLoggedIn)
                }.asPutDataRequest().setUrgent()

                // 응답 전송
                dataClient.putDataItem(putDataReq).addOnSuccessListener {
                    Log.d("AuthListenerService", "Login status response sent to watch")
                }
            }
        }
    }
}

fun checkLoginStatus(): Boolean {
    // TokenRepository 인스턴스 가져오기
    val tokenRepository = TokenRepository.getInstance()
    // accessToken 확인
    return runBlocking {
        val token = tokenRepository.getAccessToken()

        !token.isNullOrEmpty() // accessToken이 존재하면 true 반환
    }
}