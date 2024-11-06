package com.scare.data.member.dto.User

data class UserAPIResponseDTO (
    val code: String,
    val message: String,
    val data: UserInfoResponseDTO,
    val isSuccess: Boolean
)

class UserInfoResponseDTO {
    val email: String
    val profileUrl: String
    val provider: String
    val nickname: String

    constructor(email: String, nickname: String, profileUrl: String, provider: String) {
        this.email = email
        this.nickname = nickname
        this.provider = provider
        this.profileUrl = profileUrl
    }
}