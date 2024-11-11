package com.scare.data.heartrate.api.response

data class ResponseResult<T> (
    val code: String,
    val message: String,
    val data: T,
    val isSuccess: Boolean
)