package com.scare.ui.mobile.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scare.R

@Composable
fun ButtonContainer (navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        // "내 통계" 버튼
        MainTextButton(
            text = "내 통계",
            onClick = {
                navController.navigate("statistics") // navigate() 사용
            }
        )

        // "내 산책" 버튼`
        MainTextButton(
            text = "내 산책",
            onClick = {
                navController.navigate("walk")
            }
        )
    }
}

@Composable
fun MainTextButton(
    text: String,
    onClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .clickable(onClick = onClick) // 버튼 클릭 동작 설정
            .padding(8.dp)

    ) {
        Image (
            painter = painterResource(R.drawable.button),
            contentDescription = null,
            modifier = Modifier.size(32.dp) // 아이콘 크기 설정
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}