package com.example.healthinfo.domain.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.body_request.QuestionAskedBodyRequest
import com.example.healthinfo.data.remote.dto.QuestionAskedDto
import com.example.healthinfo.data.remote.dto.QuestionsTitleDto
import com.example.healthinfo.data.remote.dto.answer_dto.AnswersListDto
import com.example.healthinfo.data.remote.dto.quesion_detatil_dto.QuestionDetailDto
import kotlinx.coroutines.flow.Flow

interface QuestionTitleRepository {
    fun getQuestionsTitle():Flow<Resource<List<QuestionsTitleDto>>>
    fun askQuestion(requestBody:QuestionAskedBodyRequest):Flow<Resource<QuestionAskedDto>>
    fun getQuestionDetail(requestParam:String):Flow<Resource<QuestionDetailDto>>
    fun getAnswerList(requestParam: String):Flow<Resource<AnswersListDto>>
}