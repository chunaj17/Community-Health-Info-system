package com.example.healthcare.domain.repository

import com.example.healthcare.core.Resource
import com.example.healthcare.data.local.entity.AccessTokenEntity
import com.example.healthcare.data.remote.body.UserEmailAndPassword
import com.example.healthcare.data.remote.dto.AuthLoginDto
import com.example.healthcare.data.remote.dto.SignupDto
import kotlinx.coroutines.flow.Flow

interface AuthRepositoryInterface {
fun getAccessAndRefreshToken(userData:UserEmailAndPassword):Flow<Resource<AuthLoginDto>>
fun getSignUpStatus(userData: UserEmailAndPassword):Flow<Resource<SignupDto>>
fun getAccessToken():Flow<Resource<List<AccessTokenEntity>>>
}