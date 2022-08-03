package com.example.healthcare.ui.login

import com.example.healthcare.data.remote.dto.AuthLoginDto

data class LoginState(
    val AccessAndRefreshTokenData: AuthLoginDto? = null ,
    val isLoading: Boolean = false
)
