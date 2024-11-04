package com.scare.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDateTime(dateTimeString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
    val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)

    return dateTime.format(outputFormatter)
}

fun calculateTimeDifference(startDateTime: String, endDateTime: String): String {
    // 입력 형식 정의
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    // 문자열을 LocalDateTime으로 변환
    val start = LocalDateTime.parse(startDateTime, formatter)
    val end = LocalDateTime.parse(endDateTime, formatter)

    // 두 날짜 및 시간 차이 계산
    val duration = Duration.between(start, end)

    // 시, 분, 초로 변환
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    // 조건에 맞게 문자열을 구성
    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds) // 시, 분, 초 모두 표시
        minutes > 0 -> String.format("00:%02d:%02d", minutes, seconds)           // 분, 초만 표시
        else -> String.format("00:00:%02d", seconds)                                // 초만 표시
    }
}