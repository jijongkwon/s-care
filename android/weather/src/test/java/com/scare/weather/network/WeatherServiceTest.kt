package com.scare.weather.network

import com.google.gson.GsonBuilder
import com.scare.weather.BuildConfig
import com.scare.weather.model.request.WeatherRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceTest {

    private lateinit var weatherService: WeatherService
    private lateinit var weatherApi: WeatherApi

    @Before
    fun setUp() {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
        weatherService = WeatherService(weatherApi, BuildConfig.WEATHER_API_KEY)
    }

    @Test
    fun checkRealApiResponse() {
        // Given
        val request = WeatherRequest(
            pageNo = 1,
            numOfRows = 1000,
            dataType = "JSON",
            baseDate = "20241107",
            baseTime = "0600",
            nx = 55,
            ny = 127
        )

        // When
        val response = weatherService.getWeather(request).execute()

        // Then
        println("응답 코드: ${response.code()}")

        response.body()?.let { weatherResponse ->
            println("\n=== 응답 헤더 ===")
            println("결과 코드: ${weatherResponse.response.header.resultCode}")
            println("결과 메시지: ${weatherResponse.response.header.resultMsg}")

            println("\n=== 응답 바디 ===")
            println("총 데이터 수: ${weatherResponse.response.body.totalCount}")

            println("\n=== 날씨 데이터 ===")
            weatherResponse.response.body.items.item.forEach { item ->
                println("카테고리: ${item.category}, 값: ${item.obsrValue}")
                val description = when (item.category) {
                    "PTY" -> "강수형태"
                    "REH" -> "습도"
                    "RN1" -> "1시간 강수량"
                    "T1H" -> "기온"
                    "UUU" -> "동서바람성분"
                    "VEC" -> "풍향"
                    "VVV" -> "남북바람성분"
                    "WSD" -> "풍속"
                    else -> "알 수 없음"
                }
                println("설명: $description")
                println("---")
            }
        } ?: println("응답 바디가 null입니다")
    }
}