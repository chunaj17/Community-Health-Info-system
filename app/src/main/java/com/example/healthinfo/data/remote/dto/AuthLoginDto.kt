package com.example.healthinfo.data.remote.dto

import com.example.healthinfo.data.local.entity.AccessTokenEntity

data class AuthLoginDto(
    val access_token: String,
    val msg: String,
    val refresh_token: String,
    val userEmail:String,
    val status: String
) {
    fun  toAccessTokenEntity():AccessTokenEntity{
        return AccessTokenEntity(
            accessToken = access_token,
            email = userEmail
        )
    }
}