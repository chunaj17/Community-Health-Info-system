package com.example.healthinfo.ui.answer_list

import com.example.healthinfo.data.remote.dto.ReplayQuestionDto

data class ReplayQuestionState(
    val isLoading:Boolean = false,
    val data:ReplayQuestionDto? = null
)
