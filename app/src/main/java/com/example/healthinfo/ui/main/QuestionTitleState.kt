package com.example.healthinfo.ui.main

import com.example.healthinfo.data.remote.dto.question_title_dto.QuestionsTitleDto

data class QuestionTitleState(
    val QuestionTitleItems: QuestionsTitleDto? = null,
    val isLoading: Boolean = false
)