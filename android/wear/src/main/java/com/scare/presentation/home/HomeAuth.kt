package com.scare.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

@Composable
fun HomeAuth(onRequestLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "S-Care",
            fontSize = 18.sp,
        )

        Text(
            text = "로그인이 필요합니다",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onRequestLogin,
            modifier = Modifier.width(130.dp)
            ) {
            Text(text = "폰에서 로그인")
        }
    }
}