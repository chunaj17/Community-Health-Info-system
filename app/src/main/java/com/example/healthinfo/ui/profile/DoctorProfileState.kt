package com.example.healthinfo.ui.profile

import com.example.healthinfo.data.remote.dto.DoctorProfileDto

data class DoctorProfileState(
    val isLoading : Boolean = false,
    val data : DoctorProfileDto?  = null
)
