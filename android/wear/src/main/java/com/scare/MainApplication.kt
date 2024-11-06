package com.scare

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import com.scare.data.repository.sensor.HealthServicesRepository
import com.scare.data.repository.sensor.SensorRepository
import com.scare.service.sensor.SensorService

const val TAG = "scare wear os"
const val PERMISSION = Manifest.permission.BODY_SENSORS

class MainApplication : Application() {
    val healthServicesRepository by lazy { HealthServicesRepository(this) }
    val sensorRepository by lazy { SensorRepository(this) }

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "HeartRateServiceChannel",
            "Heart Rate Monitoring",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        Intent(applicationContext, SensorService::class.java).also {
            it.action = SensorService.Actions.START.toString()
            startService(it)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Intent(applicationContext, SensorService::class.java).also {
            it.action = SensorService.Actions.STOP.toString()
            startService(it)
        }
    }
}