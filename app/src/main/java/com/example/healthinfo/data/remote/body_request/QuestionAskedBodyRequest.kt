package com.example.healthinfo.data.remote.body_request

data class QuestionAskedBodyRequest(
    val email: String,
    val img: String?,
    val text: String,
    val title: String
)