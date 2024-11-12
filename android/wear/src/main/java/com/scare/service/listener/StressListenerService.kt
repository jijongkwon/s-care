package com.scare.service.listener

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.presentation.sensor.HeartRateManager

class StressListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/stress") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val stress = dataMap.getInt("stress")

                HeartRateManager.updateStress(stress)
                Log.d(TAG, "Received stress status: $stress")
            }
        }
    }
}