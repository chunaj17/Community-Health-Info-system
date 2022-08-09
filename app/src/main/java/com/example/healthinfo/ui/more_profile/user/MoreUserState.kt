package com.example.healthinfo.ui.more_profile.user

import com.example.healthinfo.data.remote.dto.MoreUserDto

data class MoreUserState(
    val isLoading:Boolean = false,
    val data:MoreUserDto? = null
)
