package com.example.healthinfo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.body_request.LogoutBodyRequest
import com.example.healthinfo.data.remote.body_request.ViewBodyRequest
import com.example.healthinfo.domain.repository.AuthRepositoryInterface
import com.example.healthinfo.domain.repository.QuestionTitleRepository
import com.example.healthinfo.ui.answer_list.AddViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val questionTitleRepository: QuestionTitleRepository,
    private val authRepositoryInterface: AuthRepositoryInterface
) : ViewModel() {
    private val _logoutState = MutableStateFlow(LogoutState())
    val logoutState = _logoutState.asStateFlow()
    private val ioDispatchers:CoroutineDispatcher = Dispatchers.IO
    private val _questionTitleState = MutableStateFlow(QuestionTitleState())
    val questionTitleState = _questionTitleState.asStateFlow()
    private val _tokenState = MutableStateFlow(TokenState())
    val tokenState = _tokenState.asStateFlow()
    private val _addViewState = MutableStateFlow(AddViewState())
    val addViewState = _addViewState.asStateFlow()
    private var searchJob: Job? = null
    fun getQuestionTitle() {
        searchJob = viewModelScope.launch {
            delay(500L)
            questionTitleRepository.getQuestionsTitle().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _questionTitleState.value = questionTitleState.value.copy(
                            QuestionTitleItems = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _questionTitleState.value = questionTitleState.value.copy(
                            QuestionTitleItems = result.data,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _questionTitleState.value = questionTitleState.value.copy(
                            QuestionTitleItems = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }

    }
    fun checkAccessToken() {
        searchJob = viewModelScope.launch(ioDispatchers) {
            delay(500L)
            authRepositoryInterface.getAccessTokenAndEmail().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _tokenState.value = tokenState.value.copy(
                            accessTokenData = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _tokenState.value = tokenState.value.copy(
                            accessTokenData = result.data ,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _tokenState.value = tokenState.value.copy(
                            accessTokenData = result.data ,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun logOut(userEmail:String) {
        searchJob = viewModelScope.launch(ioDispatchers) {
            delay(500L)
            authRepositoryInterface.logout(userData = LogoutBodyRequest(email = userEmail)).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _logoutState.value = logoutState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _logoutState.value = logoutState.value.copy(
                            data = result.data ,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _logoutState.value = logoutState.value.copy(
                            data = result.data ,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun addView(questionTitle: String) {
        searchJob = viewModelScope.launch(ioDispatchers) {
            delay(500L)
            questionTitleRepository.addView(requestBody = ViewBodyRequest(question_title = questionTitle)).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _addViewState.value = addViewState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _addViewState.value =addViewState.value.copy(
                            data = result.data ,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _addViewState.value =addViewState.value.copy(
                            data = result.data ,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

}