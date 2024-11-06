/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.scare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
    }
}