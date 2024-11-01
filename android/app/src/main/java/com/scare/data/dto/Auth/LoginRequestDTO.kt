package com.scare.data.dto.Auth

class LoginRequestDTO {
    val email: String
    val nickname: String
    val profileUrl: String
    val provider: String

    constructor(email: String, nickname: String, profileUrl: String) {
        this.email = email
        this.nickname = nickname
        this.profileUrl = profileUrl
        this.provider= "Google"
    }

}