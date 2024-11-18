package com.scare.service.listener

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.R
import com.scare.TAG
import com.scare.presentation.sensor.HeartRateManager

class StressListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path

                if (path == "/stress") {
                    sendUpdateStress(event)
                } else if (path == "/stress/notification") {
                    processNotificationData(event)
                }
            }
        }
    }

    private fun sendUpdateStress(event: DataEvent) {
        val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
        val stress = dataMap.getInt("stress")

        HeartRateManager.updateStress(stress)
        Log.d(TAG, "Received stress status: $stress")
    }

    private fun processNotificationData(event: DataEvent) {
        val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
        val title = dataMap.getString("title", "알림 제목 없음")
        val message = dataMap.getString("message", "알림 내용 없음")

        showNotification(title, message)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "WearableNotification"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "스트레스 지수 경고 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "워치에서 알림을 수신합니다. (30분 주기)"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stress_notification) // 아이콘 설정
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // 클릭 후 알림이 자동으로 닫히도록 설정
            .build()

        // API 33 이상에서만 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val checkSelfPermissionResult = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            if (checkSelfPermissionResult != PackageManager.PERMISSION_GRANTED) {
                Log.e("Notification", "POST_NOTIFICATIONS 권한이 없음")
                return
            }
        }

        // 알림 표시
        NotificationManagerCompat.from(this).notify(1, notification)
    }

}