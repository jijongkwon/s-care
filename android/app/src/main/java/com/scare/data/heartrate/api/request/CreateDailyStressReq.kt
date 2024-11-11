package com.scare.data.heartrate.api.request

data class CreateDailyStressReq (
    // private Long memberId;
    val recordedAt: String, // "yyyyMMdd"
    val stress: Int,
)