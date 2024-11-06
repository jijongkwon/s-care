package com.scare.weather

import com.scare.weather.model.WeatherRequest
import com.scare.weather.model.WeatherResponse
import com.scare.weather.network.WeatherService
import com.scare.weather.util.WeatherDateUtil
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import retrofit2.Response

class WeatherServiceTest : TestCase() {
    private lateinit var weatherService: WeatherService
    private lateinit var request: WeatherRequest

    @Before
    public override fun setUp() {
        weatherService = WeatherService()
        request = WeatherRequest(
            // 실제 발급받은 서비스 키를 그대로 사용
            serviceKey = "rKJCDjhFYT5aWYA%2FFKMbricUNLjUQQMYeOtgHZLF5quCNyIoBVElUcuvs9B3z7537HR2QYSZc2zIvwDOtHSftA%3D%3D",
            baseDate = WeatherDateUtil.getCurrentBaseDate(),
            baseTime = WeatherDateUtil.getCurrentBaseTime(),
            nx = 55,
            ny = 127
        )
    }

    @Test
    fun testGetWeatherResponse() {
        val latch = CountDownLatch(1)
        var response: Response<WeatherResponse>? = null
        var error: Throwable? = null

        weatherService.getWeather(request).enqueue(object : retrofit2.Callback<WeatherResponse> {
            override fun onResponse(
                call: retrofit2.Call<WeatherResponse>,
                weatherResponse: Response<WeatherResponse>
            ) {
                response = weatherResponse
                println("Response Code: ${response?.code()}")

                response?.body()?.let { weatherResponse ->
                    println("Result Code: ${weatherResponse.response.header.resultCode}")
                    println("Result Message: ${weatherResponse.response.header.resultMsg}")
                    println("Total Count: ${weatherResponse.response.body.totalCount}")

                    // 각 카테고리별 관측값 출력
                    weatherResponse.response.body.items.item.forEach { item ->
                        println("Category: ${item.category}, Value: ${item.obsrValue}")
                    }
                }

                latch.countDown()
            }

            override fun onFailure(call: retrofit2.Call<WeatherResponse>, t: Throwable) {
                error = t
                println("Error: ${t.message}")
                latch.countDown()
            }
        })

        latch.await(5, TimeUnit.SECONDS)

        assertNull("에러가 발생했습니다: ${error?.message}", error)
        assertNotNull("응답이 null입니다", response)
        assertTrue("응답이 실패했습니다: ${response?.code()}", response?.isSuccessful == true)

        // 응답 데이터 검증
        val weatherData = response?.body()
        assertNotNull("응답 바디가 null입니다", weatherData)

        weatherData?.let { weather ->
            assertEquals("00", weather.response.header.resultCode)
            assertEquals("NORMAL_SERVICE", weather.response.header.resultMsg)
            assertNotNull(weather.response.body.items.item)
            assertTrue(weather.response.body.items.item.isNotEmpty())

            // 필수 카테고리 존재 여부 확인 (기온)
            val temperature = weather.response.body.items.item.find { it.category == "T1H" }
            assertNotNull("기온 데이터가 없습니다", temperature)
        }
    }
}