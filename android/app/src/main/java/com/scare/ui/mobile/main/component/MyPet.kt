package com.scare.ui.mobile.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scare.R
import com.scare.ui.theme.Typography
import com.scare.ui.theme.White

@Composable
fun PetSentence() {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center // Box 내용을 중앙 정렬
    ) {
        Image(
            painter = painterResource(R.drawable.pet_talk),
            contentDescription = "PetTalkBalloon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(250.dp).height(100.dp)
        )
        Text(
            text = "기분이 좋은 날이에요",
            style = Typography.titleLarge.copy( // TextStyle 적용
                fontSize = 18.sp, // 크기 변경
                color = White, // 색깔 변경
                fontWeight = FontWeight.Bold // 굵기 변경
            ),
            modifier = Modifier
                .align(Alignment.Center) // Text를 Box 중앙에 정렬
        )
    }
}

@Composable
fun MyPetImage(stress: Int,
               modifier: Modifier = Modifier
                   .padding(vertical = 8.dp)
                   .fillMaxWidth()) {

    val petImage = when {
        stress <= 60 -> R.drawable.happy_dog
        stress <= 90 -> R.drawable.soso_dog
        else -> R.drawable.groomy_dog
    }

    Image(
        painter = painterResource(id = petImage),
        contentDescription = "Logo",
        modifier
            .width(60.dp)
            .height(250.dp)
    )
}