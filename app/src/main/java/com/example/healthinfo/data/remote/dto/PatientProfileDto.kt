package com.example.healthinfo.data.remote.dto

data class PatientProfileDto(
    val address: String,
    val age: Int,
    val first_name: String,
    val last_name: String,
    val profile_image: ProfileImage
)