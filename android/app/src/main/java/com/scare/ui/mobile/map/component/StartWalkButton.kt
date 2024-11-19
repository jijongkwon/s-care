package com.scare.ui.mobile.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.scare.R
import com.scare.ui.theme.White

@Composable
fun StartWalkButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = modifier.offset(x = 10.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.happy_dog_face),
                    contentDescription = "dog_face",
                    modifier = Modifier
                        .width(140.dp)
                        .height(140.dp)
                        .zIndex(10F)
                        .offset(x = (-40).dp, y = (-15).dp),
                )
                Image(
                    painter = painterResource(R.drawable.walk_start),
                    contentDescription = "walk_start",
                    modifier = Modifier
                        .clickable(onClick = onClick)
                        .width(280.dp)
                        .height(80.dp)
                        .align(Alignment.Center)
                )
                Text(
                    text = text,
                    color = White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 6.dp, y = (-4).dp)
                )
            }
        }
    }
}