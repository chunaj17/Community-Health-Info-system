package com.example.healthinfo.ui.answer_list

import com.example.healthinfo.data.remote.dto.answer_dto.AnswersListDto

data class AnswerListState(
    val isLoading: Boolean = false,
    val data: AnswersListDto? = null
)
