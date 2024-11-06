package com.scare.weather.analyzer

import com.scare.weather.enums.WeatherStatus
import com.scare.weather.model.WeatherInfo

class WeatherAnalyzer {
    fun analyzeWeather(weatherInfo: WeatherInfo): WeatherStatus {
        // 비/눈이 오는 경우 즉시 BAD 반환
        if (WeatherCriteria.isRainingOrSnowing(weatherInfo.precipitationType)) {
            return WeatherStatus.BAD
        }

        // 각 기준별 점수 계산
        var goodConditions = 0
        var moderateConditions = 0

        // 온도 체크
        when {
            WeatherCriteria.isTemperatureGood(weatherInfo.temperature) -> goodConditions++
            WeatherCriteria.isTemperatureModerate(weatherInfo.temperature) -> moderateConditions++
            else -> return WeatherStatus.BAD
        }

        // 습도 체크
        when {
            WeatherCriteria.isHumidityGood(weatherInfo.humidity) -> goodConditions++
            WeatherCriteria.isHumidityModerate(weatherInfo.humidity) -> moderateConditions++
            else -> return WeatherStatus.BAD
        }

        // 풍속 체크
        when {
            WeatherCriteria.isWindSpeedGood(weatherInfo.windSpeed) -> goodConditions++
            WeatherCriteria.isWindSpeedModerate(weatherInfo.windSpeed) -> moderateConditions++
            else -> return WeatherStatus.BAD
        }

        // 최종 상태 결정
        return when {
            goodConditions >= 2 -> WeatherStatus.GOOD
            goodConditions + moderateConditions >= 2 -> WeatherStatus.MODERATE
            else -> WeatherStatus.BAD
        }
    }
}