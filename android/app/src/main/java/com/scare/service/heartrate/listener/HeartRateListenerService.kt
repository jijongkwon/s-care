package com.scare.service.heartrate.listener

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.entity.HeartRate
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.ui.mobile.viewmodel.sensor.HeartRateManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime
import javax.inject.Inject

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
                    Log.d(TAG, "save heart rate $heartRate")
                }
            }
        }
    }

    private fun saveHeartRate(heartRate: HeartRate) {
        val db = AppDatabase.getInstance(this)
        HeartRateRepository(db).save(heartRate)
    }

}