package com.example.healthcare.domain.repository

import com.example.healthcare.core.Resource
import com.example.healthcare.data.remote.dto.QuestionsTitleDto
import kotlinx.coroutines.flow.Flow

interface QuestionTitleRepository {
    fun getQuestionsTitle():Flow<Resource<List<QuestionsTitleDto>>>
}