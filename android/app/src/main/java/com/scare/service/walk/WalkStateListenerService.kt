package com.scare.service.walk

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.data.walk.datastore.saveWalkStatus

class WalkStateListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/walkState") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val isWalk = dataMap.getBoolean("isWalk")

                saveWalkStatus(this, isWalk)
                Log.d(TAG, "Received walk status: $isWalk ")
            }
        }
    }
}