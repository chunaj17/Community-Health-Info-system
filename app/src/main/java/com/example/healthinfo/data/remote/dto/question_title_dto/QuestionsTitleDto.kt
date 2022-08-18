package com.example.healthinfo.data.remote.dto.question_title_dto

data class QuestionsTitleDto(
    val `data`: List<QuestionTitleDataDto>,
    val msg: String,
    val status: String
)