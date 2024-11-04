package com.scare.wear.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.scare.wear.presentation.theme.ScareTheme
import com.scare.wear.R
import com.scare.wear.presentation.theme.color_stress_bad
import com.scare.wear.presentation.theme.color_stress_good
import com.scare.wear.presentation.theme.color_stress_normal
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    hrValue: Double
) {
    val stressState: StressState = getStressStatus(hrValue)

    ScareTheme {
        Box(
            modifier = Modifier.run {
                fillMaxSize()
                    .background(MaterialTheme.colors.background)
            },
            contentAlignment = Alignment.Center
        ) {
            TimeText()
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
                            text = if (hrValue.isNaN()) "--" else stressState.text,
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
fun getStressStatus(stressValue: Double): StressState {
    if (stressValue < 70) {
        return StressState(
            text = stringResource(R.string.stress_good),
            color = color_stress_good,
            image = painterResource(R.drawable.pet_good),
        )
    } else if (stressValue < 130) {
        return StressState(
            text = stringResource(R.string.stress_normal),
            color = color_stress_normal,
            image = painterResource(R.drawable.pet_good),
        )
    } else {
        return StressState(
            text = stringResource(R.string.stress_bad),
            color = color_stress_bad,
            image = painterResource(R.drawable.pet_good),
        )
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(hrValue = 130.0)
}