package com.example.healthcare.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.core.Resource
import com.example.healthcare.domain.repository.AuthRepositoryInterface
import com.example.healthcare.domain.repository.QuestionTitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val questionTitleRepository: QuestionTitleRepository,
    private val authRepositoryInterface: AuthRepositoryInterface
) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val ioDispatchers:CoroutineDispatcher = Dispatchers.IO
    private val _questionTitleState = MutableStateFlow(QuestionTitleState())
    val questionTitleState = _questionTitleState.asStateFlow()
    private val _tokenState = MutableStateFlow(TokenState())
    val tokenState = _tokenState.asStateFlow()
    private var searchJob: Job? = null
    fun getQuestionTitle() {
        searchJob = viewModelScope.launch {
            delay(500L)
            questionTitleRepository.getQuestionsTitle().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _questionTitleState.value = questionTitleState.value.copy(
                            QuestionTitleItems = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _questionTitleState.value = questionTitleState.value.copy(
                            QuestionTitleItems = result.data ?: emptyList(),
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _questionTitleState.value = questionTitleState.value.copy(
                            QuestionTitleItems = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }

    }
    fun checkAccessToken() {
//        searchJob?.cancel()
        searchJob = viewModelScope.launch(ioDispatchers) {
            delay(500L)
            authRepositoryInterface.getAccessToken().onEach { result ->
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

//    override fun onCleared() {
//        super.onCleared()
//        viewModelJob.cancel()
//    }

}