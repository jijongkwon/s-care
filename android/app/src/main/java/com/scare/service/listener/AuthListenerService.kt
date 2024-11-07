package com.scare.service.listener

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.WearableListenerService

class AuthListenerService: WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/authRequest") {

                // 로그인 상태 확인
                val isLoggedIn = checkLoginStatus() // 로그인 상태 확인 함수 호출
                val putDataReq = PutDataMapRequest.create("/initialLoginResponse").apply {
                    dataMap.putBoolean("isLoggedIn", isLoggedIn)
                    dataMap.putString("userName", "User Name") // 예시로 사용자 이름 추가
                }.asPutDataRequest().setUrgent()

                // 응답 전송
                dataClient.putDataItem(putDataReq).addOnSuccessListener {
                    Log.d(TAG, "Login status response sent to watch")
                }
            }
        }
    }
}