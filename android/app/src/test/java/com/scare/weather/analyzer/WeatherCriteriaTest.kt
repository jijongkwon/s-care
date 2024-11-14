package com.scare.weather.analyzer

import com.scare.weather.core.analyzer.WeatherCriteria
import org.junit.Assert.*
import org.junit.Test

class WeatherCriteriaTest {
    @Test
    fun `온도가 좋은 날씨 범위인 경우`() {
        assertTrue(WeatherCriteria.isTemperatureGood(15.0))  // 최소값
        assertTrue(WeatherCriteria.isTemperatureGood(18.5))  // 중간값
        assertTrue(WeatherCriteria.isTemperatureGood(22.0))  // 최대값
    }

    @Test
    fun `온도가 좋은 날씨 범위를 벗어난 경우`() {
        assertFalse(WeatherCriteria.isTemperatureGood(14.9))  // 최소값 미만
        assertFalse(WeatherCriteria.isTemperatureGood(22.1))  // 최대값 초과
    }

    @Test
    fun `온도가 보통 날씨 범위인 경우`() {
        assertTrue(WeatherCriteria.isTemperatureModerate(0.0))  // 최소값
        assertTrue(WeatherCriteria.isTemperatureModerate(15.0))  // 중간값
        assertTrue(WeatherCriteria.isTemperatureModerate(30.0))  // 최대값
    }

    @Test
    fun `온도가 보통 날씨 범위를 벗어난 경우`() {
        assertFalse(WeatherCriteria.isTemperatureModerate(-0.1))  // 최소값 미만
        assertFalse(WeatherCriteria.isTemperatureModerate(30.1))  // 최대값 초과
    }

    @Test
    fun `습도가 좋은 날씨 범위인 경우`() {
        assertTrue(WeatherCriteria.isHumidityGood(40))  // 최소값
        assertTrue(WeatherCriteria.isHumidityGood(50))  // 중간값
        assertTrue(WeatherCriteria.isHumidityGood(60))  // 최대값
    }

    @Test
    fun `습도가 좋은 날씨 범위를 벗어난 경우`() {
        assertFalse(WeatherCriteria.isHumidityGood(39))  // 최소값 미만
        assertFalse(WeatherCriteria.isHumidityGood(61))  // 최대값 초과
    }

    @Test
    fun `습도가 보통 날씨 범위인 경우`() {
        assertTrue(WeatherCriteria.isHumidityModerate(60))  // 최소값
        assertTrue(WeatherCriteria.isHumidityModerate(65))  // 중간값
        assertTrue(WeatherCriteria.isHumidityModerate(70))  // 최대값
    }

    @Test
    fun `습도가 보통 날씨 범위를 벗어난 경우`() {
        assertFalse(WeatherCriteria.isHumidityModerate(59))  // 최소값 미만
        assertFalse(WeatherCriteria.isHumidityModerate(71))  // 최대값 초과
    }

    @Test
    fun `풍속이 좋은 날씨 범위인 경우`() {
        assertTrue(WeatherCriteria.isWindSpeedGood(1.0))  // 최소값
        assertTrue(WeatherCriteria.isWindSpeedGood(3.0))  // 중간값
        assertTrue(WeatherCriteria.isWindSpeedGood(5.0))  // 최대값
    }

    @Test
    fun `풍속이 좋은 날씨 범위를 벗어난 경우`() {
        assertFalse(WeatherCriteria.isWindSpeedGood(0.9))  // 최소값 미만
        assertFalse(WeatherCriteria.isWindSpeedGood(5.1))  // 최대값 초과
    }

    @Test
    fun `풍속이 보통 날씨 범위인 경우`() {
        // 낮은 범위 테스트
        assertTrue(WeatherCriteria.isWindSpeedModerate(0.5))  // 최소값
        assertTrue(WeatherCriteria.isWindSpeedModerate(0.7))  // 중간값
        assertTrue(WeatherCriteria.isWindSpeedModerate(1.0))  // 최대값

        // 높은 범위 테스트
        assertTrue(WeatherCriteria.isWindSpeedModerate(5.0))  // 최소값
        assertTrue(WeatherCriteria.isWindSpeedModerate(6.5))  // 중간값
        assertTrue(WeatherCriteria.isWindSpeedModerate(8.0))  // 최대값
    }

    @Test
    fun `풍속이 보통 날씨 범위를 벗어난 경우`() {
        assertFalse(WeatherCriteria.isWindSpeedModerate(0.4))  // 최소값 미만
        assertFalse(WeatherCriteria.isWindSpeedModerate(1.1))  // 낮은 범위 최대값 초과
        assertFalse(WeatherCriteria.isWindSpeedModerate(4.9))  // 높은 범위 최소값 미만
        assertFalse(WeatherCriteria.isWindSpeedModerate(8.1))  // 최대값 초과
    }

    @Test
    fun `풍속이 나쁜 날씨인 경우`() {
        assertTrue(WeatherCriteria.isWindSpeedBad(8.1))  // 최소값 초과
        assertTrue(WeatherCriteria.isWindSpeedBad(10.0))  // 높은 값
    }

    @Test
    fun `풍속이 나쁜 날씨가 아닌 경우`() {
        assertFalse(WeatherCriteria.isWindSpeedBad(8.0))  // 경계값
        assertFalse(WeatherCriteria.isWindSpeedBad(7.0))  // 낮은 값
    }

//    @Test
//    fun `강수 여부 테스트`() {
//        assertFalse(WeatherCriteria.isRainingOrSnowing(0))  // 강수 없음
//        assertTrue(WeatherCriteria.isRainingOrSnowing(1))   // 비
//        assertTrue(WeatherCriteria.isRainingOrSnowing(2))   // 비/눈
//        assertTrue(WeatherCriteria.isRainingOrSnowing(3))   // 눈
//    }
}