package com.example.healthinfo.ui.more_profile.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.body_request.UserBodyRequest
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
class MoreUserViewModel
@Inject
constructor(
    private val authRepositoryInterface: AuthRepositoryInterface
) : ViewModel() {
    private val _moreUserState = MutableStateFlow(MoreUserState())
    val moreUserState = _moreUserState.asStateFlow()
    private var searchJob: Job? = null
    fun moreUserRegistration(
        ps_firstName: String,
        ps_lastName: String,
        ps_age: String,
        ps_profileImg: String?,
        address_name: String,
        email: String,
    ) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            authRepositoryInterface.getMoreUserStatus(
                userData = UserBodyRequest(
                    ps_firstName, ps_lastName, ps_age, ps_profileImg, address_name, email
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _moreUserState.value =_moreUserState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _moreUserState.value =_moreUserState.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _moreUserState.value =_moreUserState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}