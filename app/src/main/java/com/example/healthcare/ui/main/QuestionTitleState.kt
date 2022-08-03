package com.example.healthcare.ui.main

import com.example.healthcare.data.remote.dto.QuestionsTitleDto

data class QuestionTitleState(
    val QuestionTitleItems: List<QuestionsTitleDto> = emptyList(),
    val isLoading: Boolean = false
)