package com.scare.data.dto.Auth

class LoginResponseDTO {
    val accessToken: String
    val refreshToken: String

    constructor(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
}