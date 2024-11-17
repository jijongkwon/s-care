package com.scare.util

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatDateTimeToRender(timestampMillis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")

    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), ZoneId.systemDefault())
        .format(formatter)
}

fun formatDateTimeToSearch(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    return dateTime.format(formatter)
}

fun formatTimeDifference(startDateTime: Long, endDateTime: Long): String {
    val duration = calculateTimeDifference(startDateTime, endDateTime)

    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds) // 시, 분, 초 모두 표시
        minutes > 0 -> String.format("00:%02d:%02d", minutes, seconds)           // 분, 초만 표시
        else -> String.format("00:00:%02d", seconds)                                // 초만 표시
    }
}

fun calculateTimeDifference(startDateTime: Long, endDateTime: Long): Duration {
    val start = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDateTime), ZoneId.systemDefault())
    val end = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDateTime), ZoneId.systemDefault())

    return Duration.between(start, end)
}