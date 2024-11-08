package com.scare.weather.network

import com.scare.weather.model.request.WeatherRequest
import com.scare.weather.model.response.WeatherResponse
import retrofit2.Call
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherService @Inject constructor(
    private val weatherApi: WeatherApi,
    private val apiKey: String
) {
    fun getWeather(request: WeatherRequest): Call<WeatherResponse> {
        val decodedServiceKey = URLDecoder.decode(apiKey, "UTF-8")

        return weatherApi.getWeather(
            serviceKey = decodedServiceKey,
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            dataType = request.dataType,
            baseDate = request.baseDate,
            baseTime = request.baseTime,
            nx = request.nx,
            ny = request.ny
        )
    }
}