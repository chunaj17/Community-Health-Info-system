package com.example.healthinfo.data.remote.dto

import com.example.healthinfo.data.local.entity.AccessTokenEntity

data class SignUpDto(
    val access_token: String,
    val msg: String,
    val refresh_token: String,
    val status: String,
    val userEmail: String
){
    fun  toAccessTokenEntity(): AccessTokenEntity {
        return AccessTokenEntity(
            accessToken = access_token,
            email = userEmail
        )
    }
}