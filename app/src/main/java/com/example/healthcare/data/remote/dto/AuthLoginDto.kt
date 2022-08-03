package com.example.healthcare.data.remote.dto

import com.example.healthcare.data.local.entity.AccessTokenEntity

data class AuthLoginDto(
    val access_token: String,
    val msg: String,
    val refresh_token: String,
    val status: String
) {
    fun  toAccessTokenEntity():AccessTokenEntity{
        return AccessTokenEntity(
            accessToken = access_token
        )
    }
}