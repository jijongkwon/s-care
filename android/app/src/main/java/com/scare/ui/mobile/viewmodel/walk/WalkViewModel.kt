package com.scare.ui.mobile.viewmodel.walk

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable
import com.scare.data.walk.datastore.*
import com.scare.data.walk.dto.LocationDTO
import com.scare.data.walk.dto.WalkRequestDTO
import com.scare.data.walk.repository.WalkRepository
import com.scare.repository.heartrate.HeartRateRepository
import com.scare.repository.location.LocationRepository
import com.scare.service.location.LocationService
import com.scare.util.formatDateTimeToSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            } catch (e: Exception) {
                Log.e("WalkViewModel", "Error fetching locations while walking", e)
            }
        }
    }

    fun handleWalkStart(context: Context) {
        viewModelScope.launch {
            updateWalkStatus(context, true)
            updateStartTime(context, formatDateTimeToSearch(LocalDateTime.now()))
//            startLocationUpdates(context)
            val serviceIntent = Intent(context, LocationService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }

    fun handleWalkEnd(context: Context, isWalkComplete: Boolean) {
        viewModelScope.launch {
            val currentTime = formatDateTimeToSearch(LocalDateTime.now())
            updateWalkStatus(context, false)
            updateEndTime(context, currentTime)

            // Stop Foreground Service
            val serviceIntent = Intent(context, LocationService::class.java)
            context.stopService(serviceIntent)

            if (isWalkComplete) {
                fetchHeartRatesWhileWalking(_walkStartTime.value, currentTime)
                fetchLocationsWhileWalking(_walkStartTime.value, currentTime)
                delay(300)
                postWalking()
            }
            delay(300)
            _locations.value = emptyList()
        }
    }

    fun postWalking() {
        viewModelScope.launch {
            try {
                if (_locations.value.size < 2 || _heartRates.value.size < 6) {
                    return@launch
                }
                val walk = WalkRequestDTO(_walkStartTime.value, _walkEndTime.value, _heartRates.value, _locations.value)
                walkRepository.postWalk(walk)
            } catch (e: Exception) {
                Log.e("WalkViewModel", "Error posting walk", e)
            }
        }
    }
}
