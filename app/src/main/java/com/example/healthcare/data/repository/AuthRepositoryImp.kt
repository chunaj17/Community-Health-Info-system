package com.example.healthcare.data.repository

import com.example.healthcare.core.Resource
import com.example.healthcare.data.local.AccessTokenDao
import com.example.healthcare.data.local.entity.AccessTokenEntity
import com.example.healthcare.data.remote.api.HealthCareApi
import com.example.healthcare.data.remote.body.UserEmailAndPassword
import com.example.healthcare.data.remote.dto.AuthLoginDto
import com.example.healthcare.data.remote.dto.SignupDto
import com.example.healthcare.domain.repository.AuthRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImp
constructor(
    private val api:HealthCareApi,
    private val dao: AccessTokenDao
):AuthRepositoryInterface{
    override fun getAccessAndRefreshToken(
       userData:UserEmailAndPassword
    ): Flow<Resource<AuthLoginDto>> = flow{
        emit(Resource.Loading())
        try {
            val remoteLogin= api.login(userData)
            emit(Resource.Success(data = remoteLogin))
            dao.insertAccessToken(remoteLogin.toAccessTokenEntity())
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

    override fun getSignUpStatus(userData: UserEmailAndPassword): Flow<Resource<SignupDto>> = flow{

        emit(Resource.Loading())
        try {
            val remoteSignup= api.signup(userData)
            emit(Resource.Success(data = remoteSignup))
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

    override fun getAccessToken(): Flow<Resource<List<AccessTokenEntity>>> = flow{
        emit(Resource.Loading())
        try {
            val checkAccessToken = dao.checkAccessToken()
            emit(Resource.Success(data = checkAccessToken))
        }catch ( e:IOException){
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "oops,something happened"
                )
            )
        }catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?:  "couldn't reach server, check your internet settings!!"
            ))
        }
    }


}