package com.scare.service.heartrate.listener

import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.WearableListenerService
import com.scare.TAG
import com.scare.service.heartrate.SaveHeartRateService
import com.scare.service.listener.checkLoginStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HeartRateListenerService : WearableListenerService() {

    @Inject
    lateinit var saveHeartRateService: SaveHeartRateService

//    private lateinit var dataClient: DataClient

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

//                if (uri.path == "/authRequest") {
//                    // 로그인 상태 확인
//                    val isLoggedIn = checkLoginStatus() // 로그인 상태 확인 함수 호출
//
//                    Log.d("AuthListenerService", "$isLoggedIn")
//
//                    val putDataReq = PutDataMapRequest.create("/authStatus").apply {
//                        dataMap.putBoolean("isLoggedIn", isLoggedIn)
//                    }.asPutDataRequest().setUrgent()
//
//                    // 응답 전송
//                    dataClient.putDataItem(putDataReq).addOnSuccessListener {
//                        Log.d("AuthListenerService", "Login status response sent to watch")
//                    }
//                }
            }
        }
    }
}