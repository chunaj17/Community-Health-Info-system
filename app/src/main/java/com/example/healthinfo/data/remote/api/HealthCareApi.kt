package com.example.healthinfo.data.remote.api

import com.example.healthinfo.data.remote.body_request.DoctorBodyRequest
import com.example.healthinfo.data.remote.body_request.LogoutBodyRequest
import com.example.healthinfo.data.remote.body_request.UserBodyRequest
import com.example.healthinfo.data.remote.body_request.UserEmailAndPassword
import com.example.healthinfo.data.remote.dto.*
import retrofit2.http.*

interface HealthCareApi {

    companion object {
        const val BASE_URL = "http:10.0.2.2:4000/api/v1/"
    }

    @GET("question")
    suspend fun getQuestionTitle():List<QuestionsTitleDto>

    @POST("Login")
    @Headers("Content-Type:application/json")
    suspend fun login(@Body requestBody: UserEmailAndPassword): AuthLoginDto

    @POST("signup")
    @Headers("Content-Type:application/json")
    suspend fun signup(@Body requestBody:UserEmailAndPassword): SignUpDto

    @POST("more/doctor")
    @Headers("Content-Type:Application/json")
    suspend fun moreDoctor(@Body requestBody:DoctorBodyRequest): MoreDoctorDto

    @POST("more/user")
    @Headers("Content-Type:Application/json")
    suspend fun moreUser(@Body requestBody:UserBodyRequest): MoreUserDto

    @HTTP(method = "DELETE", path = "logout", hasBody = true)
    @Headers("Content-Type:Application/json")
    suspend fun logout(@Body requestBody: LogoutBodyRequest): LogoutDto
}
