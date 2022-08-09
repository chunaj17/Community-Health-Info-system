package com.example.healthinfo.ui.main

import com.example.healthinfo.data.remote.dto.QuestionsTitleDto

data class QuestionTitleState(
    val QuestionTitleItems: List<QuestionsTitleDto> = emptyList(),
    val isLoading: Boolean = false
)