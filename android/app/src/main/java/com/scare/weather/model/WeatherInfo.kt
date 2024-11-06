package com.scare.weather.model

data class WeatherInfo(
    val temperature: Double,
    val humidity: Int,
    val rainfall: Double,
    val windSpeed: Double,
    val precipitationType: Int,
) {
    companion object {
        fun fromWeatherResponse(response: WeatherResponse): WeatherInfo {
            val items = response.response.body.items.item
            return WeatherInfo(
                temperature = items.find { it.category == "T1H" }?.obsrValue?.toDoubleOrNull() ?: 0.0,
                humidity = items.find { it.category == "REH" }?.obsrValue?.toIntOrNull() ?: 0,
                rainfall = items.find { it.category == "RN1" }?.obsrValue?.toDoubleOrNull() ?: 0.0,
                windSpeed = items.find { it.category == "WSD" }?.obsrValue?.toDoubleOrNull() ?: 0.0,
                precipitationType = items.find { it.category == "PTY" }?.obsrValue?.toIntOrNull() ?: 0
            )
        }
    }
}