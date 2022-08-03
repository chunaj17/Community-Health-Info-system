package com.example.healthcare.ui.signup

import com.example.healthcare.data.remote.dto.SignupDto

data class SignupState (
    val data: SignupDto? = null,
    val isLoading:Boolean = false
        )