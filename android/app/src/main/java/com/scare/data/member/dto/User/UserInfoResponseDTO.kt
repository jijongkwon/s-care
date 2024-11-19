package com.scare.data.member.dto.User

data class UserAPIResponseDTO (
    val code: String,
    val message: String,
    val data: UserInfoResponseDTO,
    val isSuccess: Boolean
)

data class UserInfoResponseDTO (
    val email: String,
    val profileUrl: String,
    val provider: String,
    val nickname: String
)