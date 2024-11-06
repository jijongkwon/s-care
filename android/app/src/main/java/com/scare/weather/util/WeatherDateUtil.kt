package com.scare.weather.util

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object WeatherDateUtil {
    fun getCurrentBaseDate(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    }

    @SuppressLint("DefaultLocale")
    fun getCurrentBaseTime(): String {
        // API 실시간 업데이트에 따라 현재시간에서 적절한 baseTime을 반환
        val now = LocalDateTime.now()
        val hour = now.hour
        val minute = now.minute

        // 매시각 45분 이후 호출할 경우 정시 발표 데이터 사용
        return if (minute >= 45) {
            String.format("%02d00", hour)
        } else {
            String.format("%02d00", if (hour == 0) 23 else hour - 1)
        }
    }
}