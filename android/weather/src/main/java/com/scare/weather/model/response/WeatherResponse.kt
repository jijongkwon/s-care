package com.scare.weather.model.response

data class WeatherResponse(
    val response: Response
) {
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
        val baseDate: String,
        val baseTime: String,
        val category: String,
        val nx: Int,
        val ny: Int,
        val obsrValue: String
    )
}