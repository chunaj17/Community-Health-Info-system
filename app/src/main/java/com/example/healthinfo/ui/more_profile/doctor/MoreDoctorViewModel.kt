package com.example.healthinfo.ui.more_profile.doctor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.body_request.DoctorBodyRequest
import com.example.healthinfo.domain.repository.AuthRepositoryInterface
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
class MoreDoctorViewModel
@Inject
constructor(
    private val authRepositoryInterface: AuthRepositoryInterface
) : ViewModel() {
    private val _moreDoctorState = MutableStateFlow(MoreDoctorState())
    val moreDoctorState = _moreDoctorState.asStateFlow()
    private var searchJob: Job? = null
    fun moreDoctorRegistration(
        Dr_age: String,
        Dr_firstName: String,
        Dr_lastName: String,
        Dr_profileImg: String?,
        address_name: String,
        email: String,
        specialization: String,
        experience: String,
        work_location: String
    ) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            authRepositoryInterface.getMoreDoctorStatus(
                userData = DoctorBodyRequest(
                    Dr_age,
                    Dr_firstName,
                    Dr_lastName,
                    Dr_profileImg,
                    address_name,
                    email,
                    experience,
                    specialization,
                    work_location
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _moreDoctorState.value = moreDoctorState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _moreDoctorState.value = moreDoctorState.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _moreDoctorState.value = moreDoctorState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}