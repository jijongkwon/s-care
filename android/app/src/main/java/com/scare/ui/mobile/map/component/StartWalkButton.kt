package com.scare.ui.mobile.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun StartWalkButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Row {
        Box(modifier = modifier) {
            Row(
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}