package com.scare.weather.network

import com.google.gson.GsonBuilder
import com.scare.BuildConfig
import com.scare.weather.model.WeatherRequest
import com.scare.weather.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder

class WeatherService {
    private val BASE_URL = "http://apis.data.go.kr/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    fun getWeather(request: WeatherRequest): Call<WeatherResponse> {
        val decodedServiceKey = URLDecoder.decode(BuildConfig.WEATHER_API_KEY, "UTF-8")

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