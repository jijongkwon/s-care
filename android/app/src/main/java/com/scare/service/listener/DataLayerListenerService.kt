package com.scare.service.listener

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.ui.mobile.viewmodel.sensor.HeartRateManager

class DataLayerListenerService: WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d(TAG, "data changed")
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                val uri = event.dataItem.uri
                if (uri.path == "/heartRate") {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val hrValue = dataMap.getDouble("hrValue")
                    val timestamp = dataMap.getLong("timestamp")  // Optional timestamp

                    // Update UI or LiveData with the new heart rate value
                    HeartRateManager.updateHeartRate(hrValue)
                    Log.d(TAG, "Received DataItem hrValue: $hrValue at $timestamp")
                }
            }
        }
    }
}