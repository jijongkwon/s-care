package com.scare.service.heartrate.listener

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.service.heartrate.SaveHeartRateService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HeartRateListenerService : WearableListenerService() {

    @Inject
    lateinit var saveHeartRateService: SaveHeartRateService

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            Log.d(TAG, "event")
            if (event.type == DataEvent.TYPE_CHANGED) {
                val uri = event.dataItem.uri
                if (uri.path == "/heartRate") {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val heartRate = dataMap.getDouble("hrValue")
                    Log.d(TAG, "save heart rate $heartRate")
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            saveHeartRateService.saveHeartRate(heartRate)
                        } catch (e: Exception) {
                            Log.e("DataLayerListenerService", "Error saving heart rate", e)
                        }
                    }
                }
            }
        }
    }
}