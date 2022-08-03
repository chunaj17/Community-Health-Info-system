package com.example.healthcare.data.repository

import com.example.healthcare.core.Resource
import com.example.healthcare.data.remote.api.HealthCareApi
import com.example.healthcare.data.remote.dto.QuestionsTitleDto
import com.example.healthcare.domain.repository.QuestionTitleRepository
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