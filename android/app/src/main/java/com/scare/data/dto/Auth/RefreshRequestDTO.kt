package com.scare.data.dto.Auth

class RefreshRequestDTO {
    val refreshToken: String

    constructor(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}