package com.example.healthinfo.domain.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.data.remote.body_request.DoctorBodyRequest
import com.example.healthinfo.data.remote.body_request.LogoutBodyRequest
import com.example.healthinfo.data.remote.body_request.UserBodyRequest
import com.example.healthinfo.data.remote.body_request.UserEmailAndPassword
import com.example.healthinfo.data.remote.dto.*
import kotlinx.coroutines.flow.Flow

interface AuthRepositoryInterface {
fun getAccessAndRefreshToken(userData:UserEmailAndPassword):Flow<Resource<AuthLoginDto>>
fun getSignUpStatus(userData: UserEmailAndPassword):Flow<Resource<SignUpDto>>
fun getAccessTokenAndEmail():Flow<Resource<List<AccessTokenEntity>>>
fun getMoreDoctorStatus(userData:DoctorBodyRequest):Flow<Resource<MoreDoctorDto>>
fun getMoreUserStatus(userData:UserBodyRequest):Flow<Resource<MoreUserDto>>
fun logout(userData:LogoutBodyRequest):Flow<Resource<LogoutDto>>
}