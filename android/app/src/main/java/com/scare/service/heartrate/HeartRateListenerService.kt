package com.scare.service.heartrate

import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.entity.HeartRate
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.ui.mobile.viewmodel.sensor.HeartRateManager
import java.time.Duration
import java.time.LocalDateTime

class HeartRateListenerService : WearableListenerService() {

    private lateinit var dataClient: DataClient

    override fun onCreate() {
        super.onCreate()
        dataClient = Wearable.getDataClient(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            Log.d(TAG, "event")
            if (event.type == DataEvent.TYPE_CHANGED) {
                val uri = event.dataItem.uri
                if (uri.path == "/heartRate") {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val heartRate = dataMap.getDouble("hrValue")
                    val heartRateEntity = HeartRate(
                        heartRate = heartRate,
                        createdAt = LocalDateTime.now()
                    )
                    saveHeartRate(heartRateEntity)
                    dispatchStress()
                    Log.d(TAG, "save heart rate $heartRate")
                }
            }
        }
    }

    private fun getDb(): AppDatabase {
        return AppDatabase.getInstance(this)
    }

    private fun saveHeartRate(heartRate: HeartRate) {
        val db = getDb()
        HeartRateRepository(db).save(heartRate)
    }

    private fun dispatchStress() {
        val db = getDb()
        val recentHeartRates = HeartRateRepository(db).getRecentHeartRates()

        val now = LocalDateTime.now()
        val heartRateValues = recentHeartRates
            .filter { Duration.between(it.createdAt, now).toMinutes() <= 30 }
            .map { it.heartRate }
            .toDoubleArray()
        var stress = -1
        if (heartRateValues.size >= 6) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this))
            }

            val py = Python.getInstance()
            val pyModule = py.getModule("calc_stress")
            val result: PyObject = pyModule.callAttr("get_single_stress", heartRateValues)

            stress = result.toInt()
        }
        HeartRateManager.updateStress(stress)
        sendToWearDevice(stress)
    }

    private fun sendToWearDevice(stress: Int) {
        val putDataReq = PutDataMapRequest.create("/stress").apply {
            dataMap.putInt("stress", stress)
            dataMap.putLong("timestamp", System.currentTimeMillis())  // Optional: add a timestamp
        }.asPutDataRequest()
            .setUrgent()

        dataClient.putDataItem(putDataReq).addOnSuccessListener {
            Log.d(TAG, "Stress is sent to wear device successfully")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to send stress to wear device", e)
        }
    }
}