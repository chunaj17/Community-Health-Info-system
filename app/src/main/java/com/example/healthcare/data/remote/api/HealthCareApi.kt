package com.example.healthcare.data.remote.api

import com.example.healthcare.data.remote.body.UserEmailAndPassword
import com.example.healthcare.data.remote.dto.AuthLoginDto
import com.example.healthcare.data.remote.dto.QuestionsTitleDto
import com.example.healthcare.data.remote.dto.SignupDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface HealthCareApi {
    @GET("question")
    suspend fun getQuestionTitle():List<QuestionsTitleDto>
    @POST("Login")
    @Headers("Content-Type:application/json")
    suspend fun login(@Body requestBody: UserEmailAndPassword):AuthLoginDto
    @POST("signup")
    @Headers("Content-Type:application/json")
    suspend fun signup(@Body requestBody:UserEmailAndPassword):SignupDto
    companion object {
        const val BASE_URL = "http:10.0.2.2:4000/api/v1/"
    }
}
