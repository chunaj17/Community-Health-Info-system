package com.example.healthinfo.data.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.api.HealthCareApi
import com.example.healthinfo.data.remote.dto.QuestionsTitleDto
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
}