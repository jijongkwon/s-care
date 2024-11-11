package com.scare.presentation.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.scare.R
import com.scare.TAG
import com.scare.presentation.sensor.HeartRateViewModel
import com.scare.presentation.sensor.HeartRateViewModelFactory
import com.scare.presentation.theme.color_stress_bad
import com.scare.presentation.theme.color_stress_good
import com.scare.presentation.theme.color_stress_normal
import com.scare.presentation.walk.WalkViewModel
import com.scare.presentation.walk.WalkViewModelFactory
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    context: Context,
    onClickStartWalk: () -> Unit
) {
    val viewModel: HeartRateViewModel = viewModel(
        factory = HeartRateViewModelFactory()
    )
    val hrValue by viewModel.hrValue.collectAsState()
    Log.d(TAG, "hrValue $hrValue")
    val scrollState = rememberScrollState()

    val stressState: StressState = getStressStatus(hrValue)

    val walkViewModel: WalkViewModel = viewModel(
        factory = WalkViewModelFactory(context)
    )

    ScreenScaffold(
        scrollState = scrollState,
    ) {
        Box(
            modifier = Modifier.run {
                fillMaxSize()
                    .background(MaterialTheme.colors.background)
            },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.height(100.dp)
                ) {
                    Image(
                        painter = stressState.image,
                        contentDescription = null,
                        Modifier.size(150.dp)
                    )
                }
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
                        val stressValueText = if (hrValue.isNaN()) "--" else hrValue.roundToInt().toString()

                        Text(
                            text = stressValueText,
                            fontSize = 24.sp,
                            color = stressState.color
                        )
                        Text(
                            text = if (hrValue.isNaN()) "" else stressState.text,
                            fontSize = 16.sp,
                            color = stressState.color
                        )
                    }
                }
                Button(
                    onClick = {
                        onClickStartWalk()
                        walkViewModel.updateWalkStatus(context, true)
                    },
                    Modifier.width(70.dp).height(40.dp)
                ) {
                    Text("산책 시작")
                }
            }
        }
    }
}

@Composable
fun getStressStatus(stressValue: Double): StressState {
    if (stressValue < 60) {
        return StressState(
            text = stringResource(R.string.stress_good),
            color = color_stress_good,
            image = painterResource(R.drawable.happy_dog_face),
        )
    } else if (stressValue < 100) {
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