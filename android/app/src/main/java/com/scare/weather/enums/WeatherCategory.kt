package com.scare.weather.enums

enum class WeatherCategory(val code: String, val description: String) {
    PRECIPITATION("PTY", "강수형태"),
    HUMIDITY("REH", "습도"),
    RAINFALL("RN1", "1시간 강수량"),
    TEMPERATURE("T1H", "기온"),
    EAST_WEST_WIND("UUU", "동서바람성분"),
    WIND_DIRECTION("VEC", "풍향"),
    SOUTH_NORTH_WIND("VVV", "남북바람성분"),
    WIND_SPEED("WSD", "풍속");

    companion object {
        fun fromCode(code: String): WeatherCategory? = values().find { it.code == code }
    }
}
