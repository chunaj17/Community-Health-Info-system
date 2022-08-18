package com.example.healthinfo.ui.ask_question

import com.example.healthinfo.data.remote.dto.QuestionAskedDto

data class AskQuestionState(
    val isLoading:Boolean = false,
    val data: QuestionAskedDto? = null
)
