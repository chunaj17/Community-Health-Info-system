package com.example.healthinfo.data.remote.body_request

data class UserBodyRequest(
    val ps_firstName: String,
    val ps_lastName: String,
    val ps_age: String,
    val ps_profileImg: String?,
    val address_name: String,
    val email: String,
)