package com.scare.weather.core.analyzer

import com.scare.weather.model.WeatherInfo
import com.scare.weather.model.enums.WeatherStatus
import javax.inject.Inject

class WeatherAnalyzer @Inject constructor() {
    fun analyzeWeather(weatherInfo: WeatherInfo): WeatherStatus {
        // 비,눈이 오는 경우 즉시 BAD 반환
        if (WeatherCriteria.isBadWeather(weatherInfo)) {
            return WeatherStatus.BAD
        }

        // 각 기준별 점수 계산
        var goodConditions = 0
        var moderateConditions = 0

        // 온도 체크
        when {
            WeatherCriteria.isTemperatureGood(weatherInfo.temperature) -> goodConditions++
            WeatherCriteria.isTemperatureModerate(weatherInfo.temperature) -> moderateConditions++
        }

        // 습도 체크
        when {
            WeatherCriteria.isHumidityGood(weatherInfo.humidity) -> goodConditions++
            WeatherCriteria.isHumidityModerate(weatherInfo.humidity) -> moderateConditions++
        }

        // 풍속 체크
        when {
            WeatherCriteria.isWindSpeedGood(weatherInfo.windSpeed) -> goodConditions++
            WeatherCriteria.isWindSpeedModerate(weatherInfo.windSpeed) -> moderateConditions++
        }

        // 최종 상태 결정
        return when {
            goodConditions >= 2 -> WeatherStatus.GOOD
            goodConditions + moderateConditions >= 2 -> WeatherStatus.MODERATE
            else -> WeatherStatus.BAD
        }
    }
}