package com.scare.weather.model.request

data class WeatherRequest(
    val pageNo: Int = 1,
    val numOfRows: Int = 1000,
    val dataType: String = "JSON",
    val baseDate: String,
    val baseTime: String,
    val nx: Int,
    val ny: Int
)