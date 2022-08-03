package com.example.healthcare.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.core.Resource
import com.example.healthcare.data.remote.body.UserEmailAndPassword
import com.example.healthcare.domain.repository.AuthRepositoryInterface
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
                    is Resource.Success -> {
                        _loginState.value = loginState.value.copy(
                            AccessAndRefreshTokenData = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _loginState.value = loginState.value.copy(
                            AccessAndRefreshTokenData = result.data,
                            isLoading = true
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