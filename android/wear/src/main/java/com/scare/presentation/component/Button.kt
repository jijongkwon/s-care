package com.scare.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

@Composable
fun WalkButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        Modifier.width(65.dp).height(35.dp)
    ) {
        Text(text)
    }
}