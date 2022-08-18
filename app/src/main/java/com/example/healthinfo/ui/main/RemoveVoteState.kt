package com.example.healthinfo.ui.main

import com.example.healthinfo.data.remote.dto.RemoveVoteDto

data class RemoveVoteState(
    val data: RemoveVoteDto? = null,
    val isLoading:Boolean = false
)
