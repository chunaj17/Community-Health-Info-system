package com.example.healthinfo.data.remote.dto

data class DoctorProfileDto(
    val address: String,
    val age: Int,
    val experience: Int,
    val first_name: String,
    val last_name: String,
    val profile_image: ProfileImage,
    val specialization: String
)