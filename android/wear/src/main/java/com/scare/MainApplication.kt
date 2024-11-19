package com.scare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.scare.service.sensor.HeartRateService

const val TAG = "scare wear os"

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Intent(applicationContext, HeartRateService::class.java).also {
            it.action = HeartRateService.Actions.START.toString()
            startService(it)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val heartRateChannel = NotificationChannel(
            R.string.heartRateChannelId.toString(),
            R.string.heartRateChannelName.toString(),
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(heartRateChannel)
    }

    override fun onTerminate() {
        super.onTerminate()
        Intent(applicationContext, HeartRateService::class.java).also {
            it.action = HeartRateService.Actions.STOP.toString()
            startService(it)
        }
    }
}