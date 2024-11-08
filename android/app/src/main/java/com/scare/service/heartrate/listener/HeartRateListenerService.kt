package com.scare.service.heartrate.listener

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.data.heartrate.dao.HeartRateDao
import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.entity.HeartRate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class HeartRateListenerService : WearableListenerService() {

    private lateinit var db: AppDatabase
    private lateinit var heartRateDao: HeartRateDao

//    @Inject
//    lateinit var saveHeartRateService: SaveHeartRateService


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
                    db = AppDatabase.getInstance(this)!!
                    heartRateDao = db.getHeartRateDao()
                    saveHearRate(heartRateEntity)
                    Log.d(TAG, "save heart rate $heartRate")
                }
            }
        }
    }

    private fun saveHearRate(heartRate: HeartRate) {
        CoroutineScope(Dispatchers.IO).launch {
            heartRateDao.save(heartRate)
        }
    }

}