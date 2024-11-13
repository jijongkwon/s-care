package com.scare.service.heartrate

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
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
import com.scare.data.heartrate.notification.datastore.LastNotificationData
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.ui.mobile.viewmodel.sensor.HeartRateManager
import com.scare.util.LocalNotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HeartRateListenerService : WearableListenerService() {

    private lateinit var db: AppDatabase
    private lateinit var dataClient: DataClient

    override fun onCreate() {
        super.onCreate()
        dataClient = Wearable.getDataClient(this)
        db = AppDatabase.getInstance(this)
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
                    val stress = dispatchStress()
                    Log.d(TAG, "save heart rate $heartRate")

                    // 알림 보내지 않음
                    if (stress < 40) return;
                    
                    CoroutineScope(Dispatchers.IO).launch {
                        if (!isExpiredLastNotificationData()) {
                            Log.d("Stress Notification is Expried", "알람이 아직 유효함.")
                            return@launch
                        }

                        Log.d("Stress Notification is Expried", "알람 전송 1시간 지남.(만료)")
                        // 알람 전송
                        showStressNotificationIfPermitted(stress)
                        // 데이터 저장
                        LastNotificationData(this@HeartRateListenerService).updateLastNotificationDate(LocalDateTime.now())
                    }
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

    private fun dispatchStress(): Int {
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
        return stress
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

    private fun showStressNotificationIfPermitted(stress: Int) {
        // 권한이 있는지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            LocalNotificationUtil.showStressNotification(this, "스트레스 지수 $stress (${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))})")
        } else {
            Log.e("HeartRateListenerService", "알림 권한이 없어 알림을 표시하지 못했습니다.")
        }
    }

    private suspend fun isExpiredLastNotificationData(): Boolean {
        // 비동기적으로 마지막 알림 날짜를 가져옵니다.
        val lastDate = LastNotificationData(this@HeartRateListenerService).lastNotificationDate.first()

        // 현재 시간을 가져옵니다.
        val now = LocalDateTime.now()

        // 마지막 알림 날짜가 null이 아니고, 1시간 이상 차이가 나는지 확인
        return lastDate?.let {
            Duration.between(it, now).toHours() >= 1
        } ?: true // lastDate가 null인 경우 true 반환 (즉, 알림이 만료됨을 의미)
    }

}