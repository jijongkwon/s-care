package com.scare.service.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.scare.data.member.repository.Auth.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FCMMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // 메시지 처리 (필요시 Notification을 표시)
        remoteMessage.notification?.let {
            Log.d("FCM Message", "Message Notification: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "Refreshed token: $token")

        // 새 토큰을 TokenRepository에 저장
        CoroutineScope(Dispatchers.IO).launch {
            TokenRepository.getInstance(applicationContext).saveFcmToken(token)
        }
    }
}