package com.scare.service.heartrate.listener

import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
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


//    private lateinit var dataClient: DataClient

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

    private fun saveHearRate(heartRate: HeartRate) {
        CoroutineScope(Dispatchers.IO).launch {
            heartRateDao.save(heartRate)
        }
    }

}