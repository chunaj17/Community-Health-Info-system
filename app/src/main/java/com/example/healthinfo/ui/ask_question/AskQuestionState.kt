package com.example.healthinfo.ui.ask_question

import com.example.healthinfo.data.remote.dto.QuestionAskedDto

data class AskQuestionState(
    val data: QuestionAskedDto? = null,
    val isLoading:Boolean = false
)
