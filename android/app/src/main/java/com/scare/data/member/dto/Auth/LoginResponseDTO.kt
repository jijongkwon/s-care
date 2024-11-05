package com.scare.data.member.dto.Auth

class LoginResponseDTO {
    val accessToken: String
    val refreshToken: String

    constructor(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
}