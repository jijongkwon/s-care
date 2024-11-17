package com.scare.ui.mobile.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scare.R
import com.scare.ui.mobile.common.LocalNavController
import com.scare.ui.theme.Gray

@Composable
fun WalkEndModal(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    handleWalkStop: () -> Unit,
    isWalkComplete: Boolean
) {

    val navController = LocalNavController.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gray.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isWalkComplete) {
                        Image(
                            painter = painterResource(R.drawable.happy_dog_face),
                            contentDescription = "happy_dog_face",
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.gloomy_dog_face),
                            contentDescription = "gloomy_dog_face",
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                        )
                    }
                    if (isWalkComplete) {
                        Text(
                            text = "산책을 종료했습니다!",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Text(
                            text = "5분 이상의 산책만 기록됩니다.\n산책을 종료할까요?",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isWalkComplete) {
                        Button(
                            onClick = { onClose() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = "닫기",
                            )
                        }
                    } else {
                        Button(
                            onClick = { handleWalkStop() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = "산책 그만하기",
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    if (isWalkComplete) {
                        Button(
                            onClick = { navController?.navigate("walk") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text(
                                text = "기록 보기",
                            )
                        }
                    } else {
                        Button(
                            onClick = { onClose() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text(
                                text = "산책 계속하기",
                            )
                        }
                    }
                }
            }
        }
    }
}