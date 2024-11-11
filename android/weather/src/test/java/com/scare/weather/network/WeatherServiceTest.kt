package com.scare.weather.network

import com.scare.weather.core.analyzer.WeatherAnalyzer
import com.scare.weather.di.WeatherModule
import com.scare.weather.model.WeatherInfo
import com.scare.weather.model.enums.WeatherStatus
import com.scare.weather.model.response.WeatherResponse
import com.scare.weather.model.util.GPSConvertor
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherServiceTest {

    private lateinit var weatherService: WeatherService
    private lateinit var weatherAnalyzer: WeatherAnalyzer

    @Before
    fun setUp() {
        // DI 모듈에서 의존성 주입
        val weatherApi = WeatherModule.provideWeatherApi(
            WeatherModule.provideRetrofit(
                WeatherModule.provideOkHttpClient(),
                WeatherModule.provideGson()
            )
        )

        weatherService =
            WeatherModule.provideWeatherService(weatherApi, WeatherModule.provideApiKey())
        weatherAnalyzer = WeatherAnalyzer()
    }

    @Test
    fun `실제 날씨 API 응답 테스트`() {
        // Given
        val request = GPSConvertor.createWeatherRequest(37.552987017, 126.972591728)

        // When
        val response = weatherService.getWeather(request).execute()

        // Then
        printWeatherResponse(response)

        // Assert
        assert(response.isSuccessful) { "API 호출이 실패했습니다. 응답 코드: ${response.code()}" }
        response.body()?.let { weatherResponse ->
            with(weatherResponse.response) {
                assert(header.resultCode == "00") { "API 응답 코드가 정상이 아닙니다: ${header.resultCode}" }
                assert(body.items.item.isNotEmpty()) { "날씨 데이터가 비어있습니다" }
            }
        } ?: throw AssertionError("응답 바디가 null입니다")
    }

    @Test
    fun `날씨 정보 분석 테스트`() {
        // Given
        val request = GPSConvertor.createWeatherRequest(37.552987017, 126.972591728)
        val response = weatherService.getWeather(request).execute()
        val weatherResponse = response.body()
            ?: throw AssertionError("응답 바디가 null입니다")

        // When
        val weatherInfo = WeatherInfo.fromWeatherResponse(weatherResponse)
        val weatherStatus = weatherAnalyzer.analyzeWeather(weatherInfo)

        // Then
        println("\n=== 날씨 분석 결과 ===")
        println("온도: ${weatherInfo.temperature}°C")
        println("습도: ${weatherInfo.humidity}%")
        println("강수량: ${weatherInfo.rainfall}mm")
        println("풍속: ${weatherInfo.windSpeed}m/s")
        println("강수형태: ${getPrecipitationTypeDescription(weatherInfo.precipitationType)}")
        println("날씨 상태: ${getWeatherStatusDescription(weatherStatus)}")
    }

    private fun printWeatherResponse(response: Response<WeatherResponse>) {
        println("응답 코드: ${response.code()}")

        response.body()?.let { weatherResponse ->
            with(weatherResponse.response) {
                println("\n=== 응답 헤더 ===")
                println("결과 코드: ${header.resultCode}")
                println("결과 메시지: ${header.resultMsg}")

                println("\n=== 응답 바디 ===")
                println("총 데이터 수: ${body.totalCount}")

                println("\n=== 날씨 데이터 ===")
                body.items.item.forEach { item ->
                    val description = getWeatherCategoryDescription(item.category)
                    println("카테고리: ${item.category} ($description)")
                    println("값: ${item.obsrValue}")
                    println("---")
                }
            }
        } ?: println("응답 바디가 null입니다")
    }

    private fun getWeatherCategoryDescription(category: String) = when (category) {
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

    private fun getPrecipitationTypeDescription(type: Int) = when (type) {
        0 -> "없음"
        1 -> "비"
        2 -> "비/눈"
        3 -> "눈"
        4 -> "소나기"
        5 -> "빗방울"
        6 -> "빗방울/눈날림"
        7 -> "눈날림"
        else -> "알 수 없음"
    }

    private fun getWeatherStatusDescription(status: WeatherStatus) = when (status) {
        WeatherStatus.GOOD -> "좋음 👍"
        WeatherStatus.MODERATE -> "보통 😐"
        WeatherStatus.BAD -> "나쁨 👎"
    }
}