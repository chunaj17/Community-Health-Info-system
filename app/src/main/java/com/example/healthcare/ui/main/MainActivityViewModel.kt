package com.example.healthcare.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.core.Resource
import com.example.healthcare.domain.repository.QuestionTitleRepository
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
class MainActivityViewModel
@Inject
constructor(
    private val questionTitleRepository: QuestionTitleRepository
) : ViewModel() {
    private val _questionTitleState = MutableStateFlow(QuestionTitleState())
    val questionTitleState = _questionTitleState.asStateFlow()
    private var searchJob: Job? = null
    fun getQuestionTitle() {
        searchJob?.cancel()
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
}