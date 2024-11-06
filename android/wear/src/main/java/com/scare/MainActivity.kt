/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.scare

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.Wearable
import com.scare.presentation.home.HomeApp

class MainActivity : ComponentActivity(), DataClient.OnDataChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val healthServicesRepository = (application as MainApplication).healthServicesRepository
        val sensorRepository = (application as MainApplication).sensorRepository

        setContent {
            HomeApp(
                healthServicesRepository = healthServicesRepository,
                sensorRepository = sensorRepository,
            )
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