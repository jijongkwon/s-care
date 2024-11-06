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
import androidx.activity.viewModels
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.scare.presentation.home.HomeApp
import com.scare.presentation.sensor.HeartRateManager
import com.scare.presentation.sensor.HeartRateViewModel

class MainActivity : ComponentActivity() {

    private val heartRateViewModel: HeartRateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HeartRateManager.setViewModel(heartRateViewModel)

        setContent {
            HomeApp()
        }

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
            val python = Python.getInstance()
            val module = python.getModule("calc_stress")
            val message: PyObject = module.callAttr(
                "get_single_stress", arrayOf(
                    70.0, 72.0, 71.0, 73.0, 75.0, 74.0, 76.0, 77.0, 78.0, 76.0, 75.0
                )
            )
        } else {
            Log.d("PythonError", "실패!")
        }
    }
}