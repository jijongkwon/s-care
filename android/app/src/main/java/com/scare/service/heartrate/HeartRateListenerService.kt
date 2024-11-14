package com.scare.service.heartrate

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import com.scare.weather.core.analyzer.WeatherAnalyzer
import com.scare.weather.core.analyzer.WeatherCriteria
import com.scare.weather.di.WeatherModule
import com.scare.weather.model.WeatherInfo
import com.scare.weather.model.util.GPSConvertor
import com.scare.weather.network.WeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HeartRateListenerService : WearableListenerService() {

    private lateinit var db: AppDatabase
    private lateinit var dataClient: DataClient
    private lateinit var weatherService: WeatherService
    private lateinit var weatherAnalyzer: WeatherAnalyzer
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        dataClient = Wearable.getDataClient(this)
        db = AppDatabase.getInstance(this)
        weatherService

        // WeatherService 초기화
        val weatherApi = WeatherModule.provideWeatherApi(
            WeatherModule.provideRetrofit(
                WeatherModule.provideOkHttpClient(),
                WeatherModule.provideGson()
            )
        )
        weatherService = WeatherModule.provideWeatherService(weatherApi, WeatherModule.provideApiKey())
        weatherAnalyzer = WeatherAnalyzer()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
                            return@launch
                        }

                        Log.d("Stress Notification is Expried", "알림 전송 가능 (1시간 지나서 만료)")

                        // 날씨 정보를 가져와 비가 오는지 확인
                        if (isBadWeather()) {
                            return@launch
                        }

                        // 알람 전송
                        showStressNotificationIfPermitted(stress, isBadWeather())
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

    private fun showStressNotificationIfPermitted(stress: Int, isBadWeather: Boolean) {
        // 권한이 있는지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            LocalNotificationUtil.showStressNotification(this, "스트레스 매우 높음!! $stress (${ LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))})", if (isBadWeather) "날씨 안 좋음!" else "날씨 좋음!")
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
        val result = lastDate?.let {
//            Duration.between(it, now).toHours() >= 1
            Duration.between(it, now).seconds >= 30 // 테스트 단계에서는 30초로 설정
        } ?: true

        if (!result) {
            Log.d("Stress Notification", "알림을 보낸지 1시간이 지나지 않았음!")
        }

        return result
    }

    private suspend fun isBadWeather(): Boolean = withContext(Dispatchers.IO) {
        var result = true;

        try {
            // 현재 위치를 가져오기 전에 위치 권한 확인
            val location = getLastKnownLocation()

            // 사용자의 현재 위치 넣기
            val request = GPSConvertor.createWeatherRequest(location.latitude, location.longitude)
            val response = weatherService.getWeather(request).execute()

            if (response.isSuccessful) {
                val weatherResponse = response.body() ?: throw NullPointerException("날씨 응답이 null입니다.")
                val weatherInfo = WeatherInfo.fromWeatherResponse(weatherResponse)
//                val weatherStatus = weatherAnalyzer.analyzeWeather(weatherInfo)

                if (WeatherCriteria.isBadWeather(weatherInfo)) {
                    Log.d("checkWeather", "날씨가 좋지 않습니다!")

                } else {
                    Log.d("checkWeather", "날씨가 매우 좋습니다!")
                    result = false
                }

            } else {
                Log.e("checkWeather", "날씨 정보를 가져오는 데 실패했습니다.")
            }

        } catch (e: NullPointerException) {
            Log.e("checkWeather", "날씨 응답이 null입니다.")
        } catch (e: Exception) {
            Log.e("checkWeather", "날씨 정보 조회 중 오류 발생", e)
        }

        result
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation() = withContext(Dispatchers.IO) {
        // 위치 권한이 있는지 확인
        if (ActivityCompat.checkSelfPermission(this@HeartRateListenerService, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this@HeartRateListenerService, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("getLastKnownLocation", "위치 권한이 필요함!!")
            throw SecurityException("위치 권한이 필요합니다!!")
        }

        // 마지막으로 알려진 위치를 가져옴
        // 마지막 알려진 위치가 없을 경우 기본 값으로 대체
        fusedLocationClient.lastLocation.await() ?: Location("default").apply {
            latitude = 37.552987017
            longitude = 126.972591728
        }
    }

}