package com.scare.data.calender.dto

data class WeeklyReportResponseDTO (
    val code: String,
    val message: String,
    val data: List<WeeklyReportDataDTO>,
    val isSuccess: Boolean,
)

data class WeeklyReportDataDTO(
    val type: String, // type 필드 추가
    val walkingCnt: Int? = null,
    val totalWalkingTime: Int? = null,
    val avgStressChange: Double? = null,
    val startIdx: Int? = null,
    val endIdx: Int? = null,
    val posList: List<WalkingDTO>? = null,
    val lastWeekStress: Double? = null,
    val currentWeekStress: Double? = null
)

data class WalkingDTO(
    val lat: Double,
    val lng: Double
)