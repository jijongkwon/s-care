package com.scare.ui.mobile.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.viewmodel.pressure.Solution
import com.scare.ui.theme.DarkNavy
import com.scare.ui.theme.NeonYellow
import com.scare.ui.theme.White

@Composable
fun SolutionCardList(solutions: List<Solution>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(solutions) { solution ->
            SolutionCard(solution)
        }
    }
}

@Composable
fun SolutionCard(solution: Solution) {
    val navController = LocalNavController.current
    val cardColor = if (isSystemInDarkTheme()) {
        DarkNavy
    } else {
        White
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        border = BorderStroke(1.dp, NeonYellow),
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp),
        onClick = {
            navController?.navigate(solution.path)
        },
    ) {
        Text(
            text = solution.title,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}