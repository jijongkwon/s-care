package com.scare.ui.mobile.main

import androidx.compose.animation.core.copy
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.minDimension
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Configuration
import com.scare.R
import com.scare.ui.mobile.common.TheHeader
import com.scare.ui.mobile.main.component.ButtonContainer
import com.scare.ui.mobile.main.component.MyPetImage
import com.scare.ui.mobile.main.component.MyStressRate
import com.scare.ui.mobile.main.component.PetSentence
import com.scare.ui.mobile.main.component.SolutionCardList
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.ScareTheme
import com.scare.ui.theme.high
import com.scare.ui.theme.low
import com.scare.ui.theme.medium
import com.scare.ui.theme.Typography // Typography import 추가

@Composable
fun MainPage(imageUrl: String?, accessToken: String?) {
    Scaffold(
        topBar = { TheHeader(imageUrl, accessToken, isMainPage = true) }
    ) { innerPadding ->
        val solutions = listOf("산책하기", "ASMR", "펫과 대화하기")
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PetSentence()
            MyPetImage(60)
            MyStressRate(60)
            ButtonContainer()
            SolutionCardList(solutions)
        }
    }
}

///////////////프리뷰 확인
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StartPagePreview() {
    ScareTheme {
        MainPage(imageUrl = null, accessToken = null) // 빈 콜백 함수 전달
    }
}