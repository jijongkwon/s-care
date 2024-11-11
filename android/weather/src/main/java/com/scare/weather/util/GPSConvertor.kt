package com.scare.weather.model.util

import com.scare.weather.model.request.WeatherRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.floor

class GPSConvertor {
    companion object {
        private const val TO_GRID = 0

        fun createWeatherRequest(latitude: Double, longitude: Double): WeatherRequest {
            // 좌표 변환
            val convertedCoord = convertToGridCoord(latitude, longitude)

            // 현재 날짜와 시간 정보 가져오기
            val now = LocalDateTime.now()

            // baseDate 포맷팅 (YYYYMMDD)
            val baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

            // baseTime 계산 - 가장 최근의 정시 시간으로 설정
            val baseTime = formatBaseTime(now)

            return WeatherRequest(
                baseDate = baseDate,
                baseTime = baseTime,
                nx = convertedCoord.x.toInt(),
                ny = convertedCoord.y.toInt()
            )
        }

        private fun convertToGridCoord(lat: Double, lng: Double): LatXLngY {
            val RE = 6371.00877 // 지구 반경(km)
            val GRID = 5.0 // 격자 간격(km)
            val SLAT1 = 30.0 // 투영 위도1(degree)
            val SLAT2 = 60.0 // 투영 위도2(degree)
            val OLON = 126.0 // 기준점 경도(degree)
            val OLAT = 38.0 // 기준점 위도(degree)
            val XO = 43 // 기준점 X좌표(GRID)
            val YO = 136 // 기준점 Y좌표(GRID)

            val DEGRAD = Math.PI / 180.0

            val re = RE / GRID
            val slat1 = SLAT1 * DEGRAD
            val slat2 = SLAT2 * DEGRAD
            val olon = OLON * DEGRAD
            val olat = OLAT * DEGRAD

            val sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) /
                    Math.log(Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5))
            val sf = Math.pow(Math.tan(Math.PI * 0.25 + slat1 * 0.5), sn) * Math.cos(slat1) / sn
            val ro = re * sf / Math.pow(Math.tan(Math.PI * 0.25 + olat * 0.5), sn)

            val rs = LatXLngY()
            rs.lat = lat
            rs.lng = lng

            val ra = re * sf / Math.pow(Math.tan(Math.PI * 0.25 + (lat) * DEGRAD * 0.5), sn)
            var theta = lng * DEGRAD - olon
            if (theta > Math.PI) theta -= 2.0 * Math.PI
            if (theta < -Math.PI) theta += 2.0 * Math.PI
            theta *= sn

            rs.x = floor(ra * Math.sin(theta) + XO + 0.5)
            rs.y = floor(ro - ra * Math.cos(theta) + YO + 0.5)

            return rs
        }

        private fun formatBaseTime(dateTime: LocalDateTime): String {
            // 현재 시간을 기준으로 가장 최근의 정시 구하기
            val hour = dateTime.hour
            // API가 매 시간 40분에 업데이트된다고 가정
            val baseHour = if (dateTime.minute < 40) {
                if (hour == 0) 23 else hour - 1
            } else {
                hour
            }

            return String.format("%02d00", baseHour)
        }
    }
}

data class LatXLngY(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var x: Double = 0.0,
    var y: Double = 0.0
)