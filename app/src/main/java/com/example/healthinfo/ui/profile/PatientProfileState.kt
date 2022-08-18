package com.example.healthinfo.ui.profile

import com.example.healthinfo.data.remote.dto.PatientProfileDto

data class PatientProfileState(
    val data : PatientProfileDto?  = null,
    val isLoading : Boolean = false
)
