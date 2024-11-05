/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.scare

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.Wearable
import com.scare.presentation.home.HomeApp

class MainActivity : ComponentActivity(), DataClient.OnDataChangedListener {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val healthServicesRepository = (application as MainApplication).healthServicesRepository
        val sensorRepository = (application as MainApplication).sensorRepository

        val sensorManager by lazy {
            getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        val sensor: Sensor ? by lazy {
            sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        }

        if (sensor == null) {
            Log.d(TAG, "sensor is not present on this device")
        } else {
            Log.d(TAG, "sensor is ${sensor?.name}")
        }

        setContent {
            HomeApp(
                healthServicesRepository = healthServicesRepository,
                sensorRepository = sensorRepository,
            )
            val permissionState = permission()

            LaunchedEffect(Unit) {
                if (permissionState.status.isGranted) {
                    Log.d(TAG, "permission granted, $permissionState")
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_DELETED) {
                Log.d(TAG, "DataItem deleted: " + event.dataItem.uri)
            } else if (event.type == DataEvent.TYPE_CHANGED) {
                Log.d(TAG, "DataItem changed: " + event.dataItem.uri.path)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun permission(): PermissionState {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.BODY_SENSORS,
        onPermissionResult = { granted -> Log.d(TAG, "permission $granted") }
    )
    return permissionState
}