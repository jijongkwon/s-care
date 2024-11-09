package com.scare.service.heartrate

import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.entity.HeartRate
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.ui.mobile.viewmodel.sensor.HeartRateManager
import java.time.LocalDateTime

class HeartRateListenerService : WearableListenerService() {

    @Inject
    lateinit var heartRateUseCase: HeartRateUseCase  //TODO : 여기서 주입이 되지 않음

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

    private fun getDb() : AppDatabase {
        return AppDatabase.getInstance(this)
    }

    private fun saveHeartRate(heartRate: HeartRate) {
        val db = getDb()
        HeartRateRepository(db).save(heartRate)
    }

    private fun dispatchStress() {
        val db = getDb()
        val recentHeartRates = HeartRateRepository(db).getRecentHeartRates()
        val heartRateValues = recentHeartRates.map { it.heartRate }.toDoubleArray()
        println(heartRateValues)
        var stress = -1
        if (recentHeartRates.size >= 10) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this))
            }
            val py = Python.getInstance()
            val pyModule = py.getModule("calc_stress")

            val result: PyObject = pyModule.callAttr("get_single_stress", heartRateValues)
            stress = result.toInt()
            Log.d(TAG, "stress 계산 ")
        }
        HeartRateManager.updateStress(stress)
    }

}