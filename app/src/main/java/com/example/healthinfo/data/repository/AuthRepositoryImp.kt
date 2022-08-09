package com.example.healthinfo.data.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.local.AccessTokenDao
import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.data.remote.api.HealthCareApi
import com.example.healthinfo.data.remote.body_request.DoctorBodyRequest
import com.example.healthinfo.data.remote.body_request.LogoutBodyRequest
import com.example.healthinfo.data.remote.body_request.UserBodyRequest
import com.example.healthinfo.data.remote.body_request.UserEmailAndPassword
import com.example.healthinfo.data.remote.dto.*
import com.example.healthinfo.domain.repository.AuthRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImp
constructor(
    private val api: HealthCareApi,
    private val dao: AccessTokenDao
) : AuthRepositoryInterface {
    override fun getAccessAndRefreshToken(
        userData: UserEmailAndPassword
    ): Flow<Resource<AuthLoginDto>> = flow {
        emit(Resource.Loading())
        try {
            val remoteLogin = api.login(userData)
            emit(Resource.Success(data = remoteLogin))
            dao.insertAccessToken(remoteLogin.toAccessTokenEntity())
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage
                        ?: "couldn't reach server, check your internet settings!!"
                )
            )
        }
    }

    override fun getSignUpStatus(userData: UserEmailAndPassword): Flow<Resource<SignUpDto>> = flow {

        emit(Resource.Loading())
        try {
            val remoteSignup = api.signup(userData)
            emit(Resource.Success(data = remoteSignup))
            dao.insertAccessToken(remoteSignup.toAccessTokenEntity())
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage
                        ?: "couldn't reach server, check your internet settings!!"
                )
            )
        }
    }

    override fun getAccessTokenAndEmail(): Flow<Resource<List<AccessTokenEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val checkAccessToken = dao.checkAccessToken()
            emit(Resource.Success(data = checkAccessToken))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops,something happened"
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage
                        ?: "couldn't reach server, check your internet settings!!"
                )
            )
        }
    }

    override fun getMoreDoctorStatus(userData: DoctorBodyRequest): Flow<Resource<MoreDoctorDto>> =
        flow {
            emit(Resource.Loading())
            try {
                val registerMoreAboutDoctor = api.moreDoctor(userData)
                emit(Resource.Success(data = registerMoreAboutDoctor))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "oops, something happened"
                    )
                )
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage
                            ?: "couldn't reach server, check your internet settings!!"
                    )
                )
            }
        }

    override fun getMoreUserStatus(userData: UserBodyRequest): Flow<Resource<MoreUserDto>> = flow {
        emit(Resource.Loading())
        try {
            val registerMoreAboutUser = api.moreUser(userData)
            emit(Resource.Success(data = registerMoreAboutUser))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage
                        ?: "couldn't reach server, check your internet settings!!"
                )
            )
        }
    }

    override fun logout(userData: LogoutBodyRequest): Flow<Resource<LogoutDto>> = flow {
        emit(Resource.Loading())
        try {
            val apiLogout = api.logout(userData)
            dao.logout()
            emit(Resource.Success(data = apiLogout))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops, something happened"
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage
                        ?: "couldn't reach server, check your internet settings!!"
                )
            )
        }
    }


}