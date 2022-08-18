package com.example.healthinfo.data.remote.body_request

data class ReplayQuestionBodyRequest(
    val answerImage: String?,
    val answerText: String,
    val email: String,
    val question_title: String
)