package com.example.healthinfo.ui.answer_list

import com.example.healthinfo.data.remote.dto.AddVoteDto

data class AddVoteState(
    val data:  AddVoteDto? = null,
    val isLoading:Boolean = false
)
