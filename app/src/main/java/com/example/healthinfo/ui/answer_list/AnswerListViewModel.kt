package com.example.healthinfo.ui.answer_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.domain.repository.QuestionTitleRepository
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
class AnswerListViewModel
@Inject
constructor(
    private val questionTitleRepository: QuestionTitleRepository
) : ViewModel() {
    private val _answerListState = MutableStateFlow(AnswerListState())
    val answerListState = _answerListState.asStateFlow()
    private val _questionDetailState = MutableStateFlow(QuestionDetailState())
    val  questionDetailState = _questionDetailState.asStateFlow()
    private var searchJob: Job? = null
    fun getQuestionDetail(question_title: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            questionTitleRepository.getQuestionDetail(
                requestParam = question_title
            ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _questionDetailState.value = questionDetailState.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _questionDetailState.value = questionDetailState.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _questionDetailState.value = questionDetailState.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
        }
    }
    fun getAnswerList(question_title: String) {
        searchJob = viewModelScope.launch {
            delay(500L)
            questionTitleRepository.getAnswerList(
                requestParam = question_title
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _answerListState.value = answerListState.value.copy(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _answerListState.value = answerListState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _answerListState.value = answerListState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}