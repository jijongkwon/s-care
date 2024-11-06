package com.scare.service.sensor

import android.app.Notification
import android.content.Intent
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import com.scare.TAG
import com.scare.data.repository.sensor.SensorRepository
import com.scare.data.repository.sensor.latestHeartRate
import kotlinx.coroutines.runBlocking

class SensorService : PassiveListenerService() {
    private lateinit var wakeLock: WakeLock
    private lateinit var repository: SensorRepository
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "service onCreate")
        repository = SensorRepository(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "service onStartCommand")
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        acquireWakeLock()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = createNotification()
        startForeground(notificationId, notification)
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "Scare::Wakelock"
        )
        wakeLock.acquire(10*60*1000L /*10 minutes*/)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "HeartRateServiceChannel")
            .setContentTitle("Heart Rate Monitoring")
            .setContentText("Monitoring heart rate in the background")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "service onDestroy")
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    enum class Actions {
        START, STOP
    }
}
