package com.scare.data.repository.sensor

/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import com.scare.TAG
import com.scare.service.sensor.SensorService
import kotlinx.coroutines.runBlocking

class HealthServicesRepository(context: Context) {
    private val healthServicesClient = HealthServices.getClient(context)
    private val passiveMonitoringClient = healthServicesClient.passiveMonitoringClient
    private val dataTypes = setOf(DataType.HEART_RATE_BPM)
    private val repository = SensorRepository(context)

    private val passiveListenerConfig = PassiveListenerConfig(
        dataTypes = dataTypes,
        shouldUserActivityInfoBeRequested = false,
        dailyGoals = setOf(),
        healthEventTypes = setOf()
    )

    private val passiveListenerCallback: PassiveListenerCallback = object : PassiveListenerCallback {
        override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
            super.onNewDataPointsReceived(dataPoints)
            runBlocking {
                dataPoints.getData(DataType.HEART_RATE_BPM).latestHeartRate()?.let {
                    Log.d(TAG, "health heart rate: $it")
                    repository.storeLatestHeartRate(it)
                }
            }
        }
    }

    fun registerForHeartRateData() {
        Log.i(TAG, "Registering listener")
        passiveMonitoringClient.setPassiveListenerCallback(
            passiveListenerConfig,
            passiveListenerCallback
        )

        passiveMonitoringClient.setPassiveListenerServiceAsync(
            SensorService::class.java,
            passiveListenerConfig
        )
    }

    fun unregisterForHeartRateData() {
        Log.i(TAG, "Unregistering listeners")
        passiveMonitoringClient.clearPassiveListenerCallbackAsync()
        passiveMonitoringClient.clearPassiveListenerServiceAsync()
    }
}
