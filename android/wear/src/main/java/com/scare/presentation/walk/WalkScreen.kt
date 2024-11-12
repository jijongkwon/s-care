package com.scare.presentation.walk

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material.MaterialTheme
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.scare.R
import com.scare.presentation.component.PetImage
import com.scare.presentation.component.WalkButton

@Composable
fun WalkScreen(
    context: Context,
    walkViewModel: WalkViewModel,
    onClickStopWalk: () -> Unit
) {
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
                    painterResource(R.drawable.happy_dog_face)
                )

                WalkButton("산책 종료") {
                    onClickStopWalk()
                    walkViewModel.updateWalkStatus(context, false)
                }
            }
        }
    }
}
