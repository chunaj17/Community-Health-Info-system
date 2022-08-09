package com.example.healthinfo.ui.more_profile.doctor

import com.example.healthinfo.data.remote.dto.MoreDoctorDto

data class MoreDoctorState(
    val data:MoreDoctorDto? = null,
    val isLoading:Boolean = false
)
