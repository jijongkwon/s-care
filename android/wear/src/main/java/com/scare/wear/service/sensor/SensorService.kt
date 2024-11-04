package com.scare.wear.service.sensor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import com.scare.wear.TAG
import com.scare.wear.data.repository.sensor.SensorRepository
import com.scare.wear.data.repository.sensor.latestHeartRate
import kotlinx.coroutines.runBlocking

class SensorService : PassiveListenerService() {
    private val repository = SensorRepository(this)
    private val channelId = "HeartRateServiceChannel"
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(notificationId, createNotification())

    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Heart Rate Monitoring")
            .setContentText("Monitoring heart rate in the background")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            channelId,
            "Heart Rate Monitoring",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        super.onNewDataPointsReceived(dataPoints)
        Log.d(TAG, "service heart rate: ${dataPoints.getData(DataType.HEART_RATE_BPM).latestHeartRate()}")
        runBlocking {
            dataPoints.getData(DataType.HEART_RATE_BPM).latestHeartRate()?.let {
                repository.storeLatestHeartRate(it)
            }
        }
    }
}
