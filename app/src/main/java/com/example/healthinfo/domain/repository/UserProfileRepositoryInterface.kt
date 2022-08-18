package com.example.healthinfo.domain.repository

import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.dto.DoctorProfileDto
import com.example.healthinfo.data.remote.dto.IdentifyUserDto
import com.example.healthinfo.data.remote.dto.PatientProfileDto
import kotlinx.coroutines.flow.Flow

interface UserProfileRepositoryInterface {
    fun getDoctorProfile(requestParam:String):Flow<Resource<DoctorProfileDto>>
    fun getPatientProfile(requestParam:String):Flow<Resource<PatientProfileDto>>
    fun identifyUser(requestParam:String):Flow<Resource<IdentifyUserDto>>

}