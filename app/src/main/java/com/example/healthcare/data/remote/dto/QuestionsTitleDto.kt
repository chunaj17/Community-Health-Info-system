package com.example.healthcare.data.remote.dto

data class QuestionsTitleDto(
    val doctor_id: Int,
    val patientId: Int,
    val question_title: String
)