package com.example.healthinfo.ui.profile

import com.example.healthinfo.data.remote.dto.IdentifyUserDto

data class UserState(
    val isLoading : Boolean = false,
    val data : IdentifyUserDto? = null
)
