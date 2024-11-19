package com.scare.data.calender.dto

data class MonthlyResponseDTO (
    val stress: Int,
    val recordedAt: String,
)

data class MonthlyStressResponseDTO (
    val code: String,
    val message: String,
    val data: List<MonthlyResponseDTO>,
    val isSuccess: Boolean
)