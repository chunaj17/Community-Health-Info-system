package com.example.healthinfo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.body_request.UserEmailAndPassword
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
class LoginViewModel
@Inject
constructor(
    private val loginRepository: AuthRepositoryInterface
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()
    private var searchJob: Job? = null
    fun authLogin(email: String, password: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            loginRepository.getAccessAndRefreshToken(
                userData = UserEmailAndPassword(
                    email,
                    password
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginState.value = loginState.value.copy(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _loginState.value = loginState.value.copy(
                            AccessAndRefreshTokenData = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _loginState.value = loginState.value.copy(
                            AccessAndRefreshTokenData = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}