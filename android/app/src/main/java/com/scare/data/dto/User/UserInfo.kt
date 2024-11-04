package com.scare.data.dto.User

class UserInfoResponseDTO{
    val email: String
    val profileUrl: String
    val nickname: String

    constructor(email: String, nickname: String, profileUrl: String) {
        this.email = email
        this.nickname = nickname
        this.profileUrl = profileUrl
    }
}