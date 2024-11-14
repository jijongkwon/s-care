package com.scare.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.scare.R
import com.scare.TAG
import com.scare.presentation.component.PetImage
import com.scare.presentation.sensor.HeartRateViewModel
import com.scare.presentation.theme.color_stress_bad
import com.scare.presentation.theme.color_stress_good
import com.scare.presentation.theme.color_stress_normal

@Composable
fun HomeScreen(
    heartRateViewModel: HeartRateViewModel,
) {
    val stress by heartRateViewModel.stress.collectAsState()

    Log.d(TAG, "stress $stress")

    val stressState: StressState = getStressStatus(stress)

    ScreenScaffold {
        Box(
            modifier = Modifier.run {
                fillMaxSize()
                    .background(MaterialTheme.colors.background)
            },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                PetImage(
                    stressState.image
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.height(50.dp)
                ) {
                    Text (
                        text = stringResource(R.string.stress),
                        fontSize = 15.sp,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stress.toString(),
                            fontSize = 24.sp,
                            color = stressState.color
                        )
                        Text(
                            text = stressState.text,
                            fontSize = 16.sp,
                            color = stressState.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getStressStatus(stress: Int): StressState {
    if (stress < 20) {
        return StressState(
            text = stringResource(R.string.stress_good),
            color = color_stress_good,
            image = painterResource(R.drawable.happy_dog_face),
        )
    } else if (stress < 40) {
        return StressState(
            text = stringResource(R.string.stress_normal),
            color = color_stress_normal,
            image = painterResource(R.drawable.normal_dog_face),
        )
    } else {
        return StressState(
            text = stringResource(R.string.stress_bad),
            color = color_stress_bad,
            image = painterResource(R.drawable.gloomy_dog_face),
        )
    }
}