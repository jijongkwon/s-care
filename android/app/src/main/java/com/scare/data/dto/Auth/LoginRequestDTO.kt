package com.scare.data.dto.Auth

data class LoginRequestDTO (
    val email: String,
    val nickname: String,
    val profileUrl: String,
    val provider: String = "Google",
)