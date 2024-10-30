package com.scare.ui.mobile.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.NeonYellow
import com.scare.ui.theme.White

@Composable
fun SolutionCardList(solutions: List<String>) {
    LazyRow( // LazyRow를 사용하여 가로 스크롤 가능한 리스트 표시
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // 카드 사이 간격 설정
    ) {
        items(solutions) { solution -> // 솔루션 데이터를 순회하며 카드 표시
            SolutionCard(solution) // SolutionCard에 솔루션 데이터 전달
        }
    }
}

@Composable
fun SolutionCard (solution: String) {
    val cardColor = if (isSystemInDarkTheme()) {
        DarkNavy
    } else {
        White
    }

    OutlinedCard (
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        border = BorderStroke(1.dp, NeonYellow),
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp)
    ) {
        Text (
            text = solution,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}
