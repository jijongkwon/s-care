package com.scare.weather.analyzer

import com.scare.weather.enums.WeatherStatus
import com.scare.weather.model.WeatherInfo
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherAnalyzerTest {
    private lateinit var weatherAnalyzer: WeatherAnalyzer

    @Before
    fun setup() {
        weatherAnalyzer = WeatherAnalyzer()
    }

    @Test
    fun `모든 조건이 좋음 범위일 때 GOOD 상태를 반환한다`() {
        val weatherInfo = WeatherInfo(
            temperature = 18.0,      // 좋음 (15.0..22.0)
            humidity = 50,           // 좋음 (40..60)
            windSpeed = 3.0,         // 좋음 (1.0..5.0)
            rainfall = 0.0,
            precipitationType = 0     // 강수 없음
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.GOOD, result)
    }

    @Test
    fun `모든 조건이 보통 범위일 때 MODERATE 상태를 반환한다`() {
        val weatherInfo = WeatherInfo(
            temperature = 25.0,      // 보통 (0.0..30.0)
            humidity = 65,           // 보통 (60..70)
            windSpeed = 6.0,         // 보통 (5.0..8.0)
            rainfall = 0.0,
            precipitationType = 0     // 강수 없음
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.MODERATE, result)
    }

    @Test
    fun `강수가 있을 때 무조건 BAD 상태를 반환한다`() {
        // 다른 조건들은 모두 좋음 범위이지만 강수가 있는 경우
        val weatherInfo = WeatherInfo(
            temperature = 18.0,
            humidity = 50,
            windSpeed = 3.0,
            rainfall = 1.0,
            precipitationType = 1     // 비가 옴
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.BAD, result)
    }

    @Test
    fun `풍속이 나쁨 범위일 때 BAD 상태를 반환한다`() {
        val weatherInfo = WeatherInfo(
            temperature = 18.0,      // 좋음
            humidity = 50,           // 좋음
            windSpeed = 8.1,         // 나쁨 (>8.0)
            rainfall = 0.0,
            precipitationType = 0
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.BAD, result)
    }

    @Test
    fun `좋음 조건과 보통 조건이 섞여 있을 때 적절한 상태를 반환한다`() {
        // 온도는 좋음, 습도는 보통, 풍속은 좋음인 경우
        val weatherInfo = WeatherInfo(
            temperature = 20.0,      // 좋음
            humidity = 65,           // 보통
            windSpeed = 3.0,         // 좋음
            rainfall = 0.0,
            precipitationType = 0
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.GOOD, result)  // 좋음 조건이 더 많으므로 GOOD
    }

    @Test
    fun `극한 기온일 때 BAD 상태를 반환한다`() {
        val weatherInfo = WeatherInfo(
            temperature = -1.0,      // 나쁨 (<0.0)
            humidity = 50,           // 좋음
            windSpeed = 3.0,         // 좋음
            rainfall = 0.0,
            precipitationType = 0
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.BAD, result)
    }

    @Test
    fun `극한 습도일 때 BAD 상태를 반환한다`() {
        val weatherInfo = WeatherInfo(
            temperature = 18.0,      // 좋음
            humidity = 71,           // 나쁨 (>70)
            windSpeed = 3.0,         // 좋음
            rainfall = 0.0,
            precipitationType = 0
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.BAD, result)
    }

    @Test
    fun `경계값에서의 상태 판단을 테스트한다`() {
        // 온도, 습도, 풍속이 모두 '좋음' 범위의 경계값인 경우
        val weatherInfo = WeatherInfo(
            temperature = 15.0,      // 좋음의 최소값
            humidity = 40,           // 좋음의 최소값
            windSpeed = 1.0,         // 좋음의 최소값
            rainfall = 0.0,
            precipitationType = 0
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.GOOD, result)
    }

    @Test
    fun `보통 범위의 경계값에서의 상태 판단을 테스트한다`() {
        // 온도, 습도, 풍속이 모두 '보통' 범위의 경계값인 경우
        val weatherInfo = WeatherInfo(
            temperature = 30.0,      // 보통의 최대값
            humidity = 70,           // 보통의 최대값
            windSpeed = 8.0,         // 보통의 최대값
            rainfall = 0.0,
            precipitationType = 0
        )

        val result = weatherAnalyzer.analyzeWeather(weatherInfo)
        assertEquals(WeatherStatus.MODERATE, result)
    }
}