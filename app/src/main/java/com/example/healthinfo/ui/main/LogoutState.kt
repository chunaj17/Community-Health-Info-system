package com.example.healthinfo.ui.main

import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.data.remote.dto.LogoutDto

data class LogoutState(
    val data:LogoutDto? = null,
    val isLoading:Boolean = false
)
