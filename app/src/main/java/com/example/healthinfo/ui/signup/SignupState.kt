package com.example.healthinfo.ui.signup

import com.example.healthinfo.data.remote.dto.SignUpDto

data class SignupState (
    val data: SignUpDto? = null,
    val isLoading:Boolean = false
        )