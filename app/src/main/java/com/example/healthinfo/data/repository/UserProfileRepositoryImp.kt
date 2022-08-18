package com.example.healthinfo.data.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.api.HealthCareApi
import com.example.healthinfo.data.remote.dto.DoctorProfileDto
import com.example.healthinfo.data.remote.dto.IdentifyUserDto
import com.example.healthinfo.data.remote.dto.PatientProfileDto
import com.example.healthinfo.domain.repository.UserProfileRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class UserProfileRepositoryImp(
    private val api: HealthCareApi
): UserProfileRepositoryInterface{
    override fun getDoctorProfile(requestParam: String): Flow<Resource<DoctorProfileDto>> = flow {
        emit(Resource.Loading())
        try {
            val remoteResult = api.getDoctorProfile(requestParam)
            emit(Resource.Success(data = remoteResult))
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

    override fun getPatientProfile(requestParam: String): Flow<Resource<PatientProfileDto>> = flow{
        emit(Resource.Loading())
        try {
            val remoteResult = api.getPatientProfile(requestParam)
            emit(Resource.Success(data = remoteResult))
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

    override fun identifyUser(requestParam: String): Flow<Resource<IdentifyUserDto>> = flow {

        emit(Resource.Loading())
        try {
            val remoteResponse = api.identifyUser(requestParam)
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