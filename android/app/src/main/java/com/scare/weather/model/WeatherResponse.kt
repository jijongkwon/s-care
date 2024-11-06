package com.scare.weather.model

data class WeatherResponse(
    val response: Response
)

data class Response(
    val header: Header,
    val body: Body
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class Body(
    val dataType: String,
    val items: Items,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class Items(
    val item: List<WeatherItem>
)

data class WeatherItem(
    val baseDate: String,    // 발표일자
    val baseTime: String,    // 발표시각
    val category: String,    // 자료구분코드
    val nx: Int,            // 예보지점 X좌표
    val ny: Int,            // 예보지점 Y좌표
    val obsrValue: String   // 관측값
)