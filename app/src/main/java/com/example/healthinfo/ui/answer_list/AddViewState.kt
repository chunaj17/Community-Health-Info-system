package com.example.healthinfo.ui.answer_list


import com.example.healthinfo.data.remote.dto.ViewDto

data class AddViewState(
    val data:ViewDto? = null,
    val isLoading:Boolean = false
)