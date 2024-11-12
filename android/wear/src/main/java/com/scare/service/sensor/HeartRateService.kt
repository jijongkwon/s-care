package com.scare.service.sensor

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.scare.R
import com.scare.TAG

class HeartRateService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private lateinit var dataClient: DataClient

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "service onCreate")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        dataClient = Wearable.getDataClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "service onStartCommand")
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
            val hrValue = event.values[0].toDouble()
            if (hrValue > 0) {
                sendToHandheldDevice(hrValue)
            }
            Log.d(TAG, "service hrValue: $hrValue")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "service onDestroy")
        sensorManager.unregisterListener(this)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, R.string.heartRateChannelId.toString())
            .setContentTitle("Heart Rate Monitoring")
            .setContentText("Monitoring heart rate in the background")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()

        heartRateSensor?.let { it ->
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL).also {
                if (it) {
                    startForeground(1, notification)
                }
            }
        }
    }

    private fun sendToHandheldDevice(hrValue: Double) {
        val putDataReq = PutDataMapRequest.create("/heartRate").apply {
            dataMap.putDouble("hrValue", hrValue)
            dataMap.putLong("timestamp", System.currentTimeMillis())  // Optional: add a timestamp
        }.asPutDataRequest()
            .setUrgent()

        dataClient.putDataItem(putDataReq).addOnSuccessListener {
            Log.d(TAG, "HrValue is sent to handheld device successfully")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to send hrValue to handheld device", e)
        }
    }

    enum class Actions {
        START, STOP
    }
}
