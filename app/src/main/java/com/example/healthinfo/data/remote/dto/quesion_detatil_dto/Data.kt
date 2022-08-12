package com.example.healthinfo.data.remote.dto.quesion_detatil_dto

data class Data(
    val doctor_id: Int,
    val patientId: Int,
    val question_img: QuestionImg,
    val question_text: String
)