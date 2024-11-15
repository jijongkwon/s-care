package com.scare.ui.mobile.viewmodel.walk

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable
import com.scare.TAG
import com.scare.data.walk.datastore.getWalkEndTime
import com.scare.data.walk.datastore.getWalkStartTime
import com.scare.data.walk.datastore.getWalkStatus
import com.scare.data.walk.datastore.saveWalkEndTime
import com.scare.data.walk.datastore.saveWalkStartTime
import com.scare.data.walk.datastore.saveWalkStatus
import com.scare.data.walk.dto.LocationDTO
import com.scare.data.walk.dto.WalkRequestDTO
import com.scare.data.walk.repository.WalkRepository
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.repository.location.LocationRepository
import com.scare.util.formatDateTimeToSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class WalkViewModel(
    context: Context,
    private val heartRateRepository: HeartRateRepository,
    private val locationRepository: LocationRepository,
    private val walkRepository: WalkRepository
) : ViewModel() {

    private val dataClient = Wearable.getDataClient(context)

    private val _isWalk = MutableStateFlow(false)
    val isWalk: StateFlow<Boolean> = _isWalk

    private val _walkStartTime = MutableStateFlow("")
    val walkStartTime: StateFlow<String> = _walkStartTime

    private val _walkEndTime = MutableStateFlow("")
    val walkEndTime: StateFlow<String> = _walkEndTime

    private val _heartRates = MutableStateFlow<List<Double>>(emptyList())
    val heartRates: StateFlow<List<Double>> get() = _heartRates

    private val _locations = MutableStateFlow<List<LocationDTO>>(emptyList())
    val locations: StateFlow<List<LocationDTO>> get() = _locations

    private var _locationManager: LocationManager? = null
    private var _locationListener: LocationListener? = null

    init {
        // DataStore에서 산책 상태를 관찰
        viewModelScope.launch {
            getWalkStatus(context).collect { isWalkStatus ->
                _isWalk.value = isWalkStatus
            }
            getWalkStartTime(context).collect { walkStartTime ->
                _walkStartTime.value = walkStartTime
            }
            getWalkEndTime(context).collect { walkEndTime ->
                _walkEndTime.value = walkEndTime
            }
        }
    }

    // 산책 상태 저장
    fun updateWalkStatus(context: Context, isWalk: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveWalkStatus(context, isWalk)
            _isWalk.value = isWalk
        }
    }

    suspend fun updateStartTime(context: Context, startTime: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveWalkStartTime(context, startTime)
            _walkStartTime.value = startTime
        }
    }

    fun updateEndTime(context: Context, endTime: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveWalkEndTime(context, endTime)
            _walkEndTime.value = endTime
        }
    }

    fun fetchHeartRatesWhileWalking(startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val heartRates = heartRateRepository.getHeartRatesWhileWalking(startDate, endDate)
                Log.d("WalkViewModel", heartRates.toString())
                _heartRates.value = heartRates.map { heartRate -> heartRate.heartRate }
            } catch (e: Exception) {
                Log.e("WalkViewModel", "Error fetching heartrates while walking", e)
            }
        }
    }

    fun fetchLocationsWhileWalking(startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val locations = locationRepository.getLocations(startDate, endDate)
                _locations.value = locations.map { location -> LocationDTO(location.latitude, location.longitude) }
                locations.forEach { location ->
                    Log.d(TAG, "location $location")
                }
                _locations.value.forEach { location ->
                    Log.d(TAG, "_location $location")
                }
            } catch (e: Exception) {
                Log.e("WalkViewModel", "Error fetching locations while walking", e)
            }
        }
    }

    // 위치 업데이트 초기화
    suspend fun startLocationUpdates(context: Context) {
        // LocationManager 초기화
        Log.d(TAG, "init before")
        _locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d(TAG, "init after")
        // LocationListener 정의
        _locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 위치가 변경될 때 호출
                Log.d(TAG, "before save location $location")
                locationRepository.save(
                    com.scare.data.location.database.entity.Location(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        createdAt = LocalDateTime.now(),
                    )
                )
                Log.d(TAG, "save success location $location")
            }
        }

        // 위치 업데이트 요청
        try {
            Log.d(TAG, "before update")
            _locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, // GPS를 사용
                5000L, // 최소 업데이트 간격 (1초)
                1f, // 최소 거리 변화 (1미터)
                _locationListener!!
            )
            Log.d(TAG, "after update ")
        } catch (e: SecurityException) {
            // 위치 권한 예외 처리
            e.printStackTrace()
        }
    }

    // 위치 업데이트 중지
    fun stopLocationUpdates() {
        _locationListener?.let {
            _locationManager?.removeUpdates(it)
        }
        _locationManager = null
        _locationListener = null
    }

    fun handleWalkStart(context: Context) {
        viewModelScope.launch {
            updateWalkStatus(context, true)
            updateStartTime(context, formatDateTimeToSearch(LocalDateTime.now()))
            startLocationUpdates(context)
        }
    }

    fun handleWalkEnd(context: Context) {
        viewModelScope.launch {
            val currentTime = formatDateTimeToSearch(LocalDateTime.now())
            Log.d(TAG, "@@@@@@@@@@time = ${_walkStartTime.value} #### $currentTime")
            updateWalkStatus(context, false)
            updateEndTime(context, currentTime)
            Log.d(TAG, "handleWalkEnd ${_walkStartTime.value} $currentTime")
            Log.d(TAG, "walk - stopLocationUpdates")
            stopLocationUpdates()
            Log.d(TAG, "walk - fetchHeartRatesWhileWalking")
            fetchHeartRatesWhileWalking(_walkStartTime.value, currentTime)
            Log.d(TAG, "walk - fetchLocationsWhileWalking")
            fetchLocationsWhileWalking(_walkStartTime.value, currentTime)
            Log.d(TAG, "walk - postWalking")
            postWalking()
        }
    }

    fun postWalking() {
        viewModelScope.launch {
            try {
                val walk = WalkRequestDTO(_walkStartTime.value, _walkEndTime.value, _heartRates.value, _locations.value)
                Log.d(TAG, "walk dto $walk")
                Log.d(TAG, "before post walking ${_locations.value.size} ${_heartRates.value.size}")
                if (_locations.value.size < 2 || _heartRates.value.size < 6) {
                    return@launch
                }
//                val walk = WalkRequestDTO(_walkStartTime.value, _walkEndTime.value, _heartRates.value, _locations.value)
//                Log.d(TAG, "walk dto $walk")
                walkRepository.postWalk(walk)
            } catch (e: Exception) {
                Log.e("WalkViewModel", "Error posting walk", e)
            }
        }
    }
}
