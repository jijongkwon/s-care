package com.scare.presentation.walk

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.scare.R

@Composable
fun WalkScreen(
    onClickStopWalk: () -> Unit
) {
    val scrollState = rememberScrollState()

    ScreenScaffold(
        scrollState = scrollState
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
                Image(
                    painter = painterResource(R.drawable.happy_dog_face),
                    contentDescription = null,
                    Modifier.size(100.dp)
                )
                Button(
                    onClick = onClickStopWalk,
                    Modifier.width(70.dp).height(40.dp)
                ) {
                    Text("산책 종료")
                }
            }
        }
    }
}
