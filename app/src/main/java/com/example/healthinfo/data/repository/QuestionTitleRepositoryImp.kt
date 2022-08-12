package com.example.healthinfo.data.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.api.HealthCareApi
import com.example.healthinfo.data.remote.body_request.QuestionAskedBodyRequest
import com.example.healthinfo.data.remote.dto.QuestionAskedDto
import com.example.healthinfo.data.remote.dto.QuestionsTitleDto
import com.example.healthinfo.data.remote.dto.answer_dto.AnswersListDto
import com.example.healthinfo.data.remote.dto.quesion_detatil_dto.QuestionDetailDto
import com.example.healthinfo.domain.repository.QuestionTitleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class QuestionTitleRepositoryImp(
    private val api: HealthCareApi
) : QuestionTitleRepository {
    override fun getQuestionsTitle(): Flow<Resource<List<QuestionsTitleDto>>> = flow {
        emit(Resource.Loading())
        try {
            val remoteQuestionTitle = api.getQuestionTitle()
            emit(Resource.Success(data = remoteQuestionTitle))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?:  "couldn't reach server, check your internet settings!!"
            ))
        }
    }

    override fun askQuestion(requestBody: QuestionAskedBodyRequest): Flow<Resource<QuestionAskedDto>> = flow{
        emit(Resource.Loading())
        try {
            val remoteResponse = api.askQuestion(requestBody)
            emit(Resource.Success(data = remoteResponse))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?:  "couldn't reach server, check your internet settings!!"
            ))
        }
    }

    override fun getQuestionDetail(requestParam: String): Flow<Resource<QuestionDetailDto>> = flow{
        emit(Resource.Loading())
        try {
            val remoteResponse = api.getQuestionDetail(requestParam)
            emit(Resource.Success(data = remoteResponse))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?:  "couldn't reach server, check your internet settings!!"
            ))
        }
    }

    override fun getAnswerList(requestParam: String): Flow<Resource<AnswersListDto>> = flow{
        emit(Resource.Loading())
        try {
            val remoteResponse = api.getAnswersToQuestion(requestParam)
            emit(Resource.Success(data = remoteResponse))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?:  "couldn't reach server, check your internet settings!!"
            ))
        }
    }


}