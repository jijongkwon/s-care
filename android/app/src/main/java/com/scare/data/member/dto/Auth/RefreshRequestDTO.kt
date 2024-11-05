package com.scare.data.member.dto.Auth

class RefreshRequestDTO {
    val refreshToken: String

    constructor(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}