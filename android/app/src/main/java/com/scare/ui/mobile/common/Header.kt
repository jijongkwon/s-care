package com.scare.ui.mobile.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.scare.R

@Preview
@Composable
fun TheHeader(
    imageUrl: String? = null,
    isMainPage: Boolean = false,
) {

    val navController = LocalNavController.current

    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).statusBarsPadding().padding(horizontal = 21.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        if (isMainPage && imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "User Image",
                modifier = Modifier.size(48.dp).fillMaxWidth().clip(CircleShape) // 동그랗게 크롭
                    .clickable(onClick = { navController?.navigate("mypage") }),
                contentScale = ContentScale.Crop // 이미지가 원에 맞게 크롭되도록 설정
            )
        } else {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Back")
            }
        }

        Row {
            Logo(modifier = Modifier.clickable(onClick = { navController?.navigate("main") }))
            // 오른쪽에 비어있는 공간
            Spacer(modifier = Modifier.size(16.dp)) // 아이콘 크기와 동일한 크기의 Spacer
        }

    }
}

@Composable
fun Logo(modifier: Modifier = Modifier) {
    val logoImage = if (isSystemInDarkTheme()) {
        painterResource(R.drawable.logo_white) // 다크 모드: 흰색 로고
    } else {
        painterResource(R.drawable.logo_black) // 라이트 모드: 검은색 로고
    }

    Image(
        painter = logoImage,
        contentDescription = "Logo",
        modifier = modifier.width(80.dp) // 가로 크기 200dp로 조정
            .height(30.dp) // 세로 크기 60dp로 고정 // 크기 조정
    )
}