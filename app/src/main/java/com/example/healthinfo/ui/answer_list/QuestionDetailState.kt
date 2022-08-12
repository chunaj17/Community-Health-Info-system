package com.example.healthinfo.ui.answer_list

import com.example.healthinfo.data.remote.dto.quesion_detatil_dto.QuestionDetailDto

data class QuestionDetailState(
    val isLoading:Boolean = false,
    val data:QuestionDetailDto? = null
)
