package com.scare.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.scare.R
import com.scare.ui.theme.high
import com.scare.ui.theme.low
import com.scare.ui.theme.medium

//몸까지 있는 펫 이미지
@Composable
fun getPetImage(stress: Int): Painter {
    val petImage = when {
        stress <= 20 -> R.drawable.happy_dog
        stress <= 40 -> R.drawable.soso_dog
        else -> R.drawable.gloomy_dog
    }

    return painterResource(id = petImage)
}

//강아지 얼굴만
@Composable
fun getPetFace(stress: Int): Painter {
    val petFace = when {
        stress == 0 -> R.drawable.no_stress_data
        stress <= 20 -> R.drawable.happy_dog_face
        stress <= 40 -> R.drawable.normal_dog_face
        else -> R.drawable.gloomy_dog_face
    }

    return painterResource(id = petFace)
}


fun getStressLevel(stress:Int): String {
    val stressText = when {
        stress <= 20 -> "낮음"
        stress <= 40 -> "보통"
        else -> "높음"
    }

    return stressText
}

fun getTextColor(stressLevel: String): Color {
    val textColor = when (stressLevel) {
        "낮음" -> low
        "보통" -> medium
        else -> high
    }

    return textColor
}
