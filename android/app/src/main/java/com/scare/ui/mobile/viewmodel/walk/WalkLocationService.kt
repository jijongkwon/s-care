package com.scare.ui.mobile.viewmodel.walk

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.scare.data.location.database.LocationDatabase
import com.scare.repository.location.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import com.scare.data.location.database.entity.Location as LocationEntity

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var locationRepository: LocationRepository

    companion object {
        const val CHANNEL_ID = "LocationServiceChannel"
        const val CHANNEL_NAME = "Location Tracking"
        const val NOTIFICATION_ID = 1
        const val LOCATION_UPDATE_INTERVAL = 1000L // 1 seconds
        const val FASTEST_LOCATION_INTERVAL = 1000L // 2 seconds
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    handleNewLocation(location)
                }
            }
        }

        // Initialize LocationRepository
        val locationDatabase = LocationDatabase.getInstance(applicationContext)
        locationRepository = LocationRepository(locationDatabase)

        // Start Foreground Service
        startForegroundService()
        startLocationUpdates()
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tracking your location")
            .setContentText("Location updates are active.")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation) // Replace with your app's location icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, // 우선순위를 설정
            LOCATION_UPDATE_INTERVAL // 업데이트 간격 설정
        )
            .setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL) // 최소 업데이트 간격 설정
            .setWaitForAccurateLocation(false) // 정확한 위치를 기다릴지 여부 설정 (기본값: false)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (e: SecurityException) {
            Log.e("LocationService", "위치 권한이 허용되지 않았습니다.", e)
        }
    }

    private fun handleNewLocation(location: Location) {
        // Save location to database
        coroutineScope.launch {
            try {
                val locationEntity = LocationEntity(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    createdAt = LocalDateTime.now()
                )
                locationRepository.save(locationEntity)
                Log.d("LocationService", "Location saved: $locationEntity")
            } catch (e: Exception) {
                Log.e("LocationService", "Error saving location", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("LocationService", "Location updates stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
