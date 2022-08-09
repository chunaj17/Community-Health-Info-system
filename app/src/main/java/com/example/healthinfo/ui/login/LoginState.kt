package com.example.healthinfo.ui.login

import com.example.healthinfo.data.remote.dto.AuthLoginDto

data class LoginState(
    val AccessAndRefreshTokenData: AuthLoginDto? = null ,
    val isLoading: Boolean = false
)
