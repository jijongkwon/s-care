package com.scare.ui.mobile.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scare.R
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.mobile.viewmodel.pressure.Solution
import com.scare.ui.theme.Brown

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

    val borderColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Brown
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp),
        onClick = {
            navController?.navigate(solution.path)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val buttonImage = if (isSystemInDarkTheme()) {
                R.drawable.button_dark
            } else {
                R.drawable.button_light
            }
            Text(
                text = solution.title,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
            Image(
                painter = painterResource(buttonImage),
                contentDescription = null,
                modifier = Modifier.size(80.dp) // 아이콘 크기 설정
            )
        }
    }
}