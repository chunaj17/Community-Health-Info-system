package com.example.healthinfo.data.remote.dto.answer_dto

data class AnswersListDto(
    val data: List<DataDto>,
    val msg: String,
    val status: String
)