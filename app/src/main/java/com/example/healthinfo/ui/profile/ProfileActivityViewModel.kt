package com.example.healthinfo.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.domain.repository.UserProfileRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileActivityViewModel
@Inject
constructor(
    private val userProfileRepositoryInterface: UserProfileRepositoryInterface
) : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()
    private val _patientProfileState = MutableStateFlow(PatientProfileState())
    val patientProfileState = _patientProfileState.asStateFlow()
    private val _doctorProfileState = MutableStateFlow(DoctorProfileState())
    val doctorProfileState = _doctorProfileState.asStateFlow()
    var searchJob: Job? = null
    fun identifyUser(email: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            userProfileRepositoryInterface.identifyUser(requestParam = email).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _userState.value = userState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _userState.value = userState.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _userState.value = userState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                }

            }.launchIn(this)
        }
    }
    fun getDoctorProfile(email: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            userProfileRepositoryInterface.getDoctorProfile(requestParam = email).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _doctorProfileState.value = doctorProfileState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _doctorProfileState.value = doctorProfileState.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _doctorProfileState.value = doctorProfileState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                }

            }.launchIn(this)
        }
    }
    fun getPatientProfile(email: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            userProfileRepositoryInterface.getPatientProfile(requestParam = email).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _patientProfileState.value = patientProfileState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _patientProfileState.value = patientProfileState.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _patientProfileState.value = patientProfileState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                }

            }.launchIn(this)
        }
    }
}