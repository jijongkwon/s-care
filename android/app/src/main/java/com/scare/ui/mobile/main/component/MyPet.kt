package com.scare.ui.mobile.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scare.R
import com.scare.ui.theme.Typography
import com.scare.util.getPetImage
import com.scare.util.getStressSentence

@Composable
fun PetSentence(
    stress: Int
) {
    val sentense = getStressSentence(stress)

    val petTalkImage = if (isSystemInDarkTheme()) {
        R.drawable.pet_talk
    } else {
        R.drawable.pet_talk_light
    }

    Box(
        modifier = Modifier,
    ) {
        Image(
            painter = painterResource(petTalkImage),
            contentDescription = "PetTalkBalloon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(250.dp).height(100.dp)
        )
        Text(
            text = sentense,
            style = Typography.titleLarge.copy( // TextStyle 적용
                fontSize = 18.sp, // 크기 변경
                color = MaterialTheme.colorScheme.onSurface, // 색깔 변경
                fontWeight = FontWeight.Bold // 굵기 변경
            ),
            modifier = Modifier
                .align(Alignment.Center) // Text를 Box 중앙에 정렬
        )
    }
}

@Composable
fun MyPetImage(
    stress: Int,
    modifier: Modifier = Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()
) {

    val petImage = getPetImage(stress)

    Image(
        painter = petImage,
        contentDescription = "Logo",
        modifier
            .width(60.dp)
            .height(250.dp)
    )
}