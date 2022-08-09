package com.example.healthinfo.domain.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.dto.QuestionsTitleDto
import kotlinx.coroutines.flow.Flow

interface QuestionTitleRepository {
    fun getQuestionsTitle():Flow<Resource<List<QuestionsTitleDto>>>
}