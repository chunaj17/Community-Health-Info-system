package com.example.healthinfo.data.remote.body_request

data class DoctorBodyRequest(
    val Dr_age: String,
    val Dr_firstName: String,
    val Dr_lastName: String,
    val Dr_profileImg: String? = null,
    val address_name: String,
    val email: String,
    val experience: String,
    val specialization: String,
    val work_location: String
)