package com.example.healthinfo.ui.answer_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.body_request.AddVoteRequestBody
import com.example.healthinfo.data.remote.body_request.RemoveVoteRequestBody
import com.example.healthinfo.data.remote.body_request.ReplayQuestionBodyRequest
import com.example.healthinfo.domain.repository.AuthRepositoryInterface
import com.example.healthinfo.domain.repository.QuestionTitleRepository
import com.example.healthinfo.ui.main.RemoveVoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AnswerListViewModel
@Inject
constructor(
    private val questionTitleRepository: QuestionTitleRepository,
    private val authRepositoryInterface: AuthRepositoryInterface
) : ViewModel() {
    private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
    private val _answerListState = MutableStateFlow(AnswerListState())
    val answerListState = _answerListState.asStateFlow()
    private val _questionDetailState = MutableStateFlow(QuestionDetailState())
    val questionDetailState = _questionDetailState.asStateFlow()
    private val _replayQuestionState = MutableStateFlow(ReplayQuestionState())
    val replayQuestionState = _replayQuestionState.asStateFlow()
    private val _addVoteState = MutableStateFlow(AddVoteState())
    val addVoteState = _addVoteState.asStateFlow()
    private val _removeVoteState = MutableStateFlow(RemoveVoteState())
    val removeVoteState = _removeVoteState.asStateFlow()
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

    fun replayQuestion(
        email: String,
        question_title: String,
        answer_text: String,
        answerImage: String?
    ) {
        searchJob = viewModelScope.launch {
            delay(500L)
            questionTitleRepository.replayQuestion(
                requestBody =
                ReplayQuestionBodyRequest(
                    answerImage = answerImage,
                    answerText = answer_text,
                    email = email,
                    question_title = question_title
                )
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _replayQuestionState.value = _replayQuestionState.value.copy(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _replayQuestionState.value = _replayQuestionState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _replayQuestionState.value = _replayQuestionState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
    fun addVote(email:String, answerText:String) {
        searchJob = viewModelScope.launch(ioDispatchers) {
            delay(500L)
            questionTitleRepository.addVote(requestBody = AddVoteRequestBody(email = email, answerText = answerText)).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _addVoteState.value = addVoteState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _addVoteState.value =addVoteState.value.copy(
                            data = result.data ,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _addVoteState.value = addVoteState.value.copy(
                            data = result.data ,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
    fun removeVote(email: String, answerText: String) {
        searchJob = viewModelScope.launch(ioDispatchers) {
            delay(500L)
            questionTitleRepository.removeVote(requestBody = RemoveVoteRequestBody(email = email, answerText = answerText)).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _removeVoteState.value = removeVoteState.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _removeVoteState.value = removeVoteState.value.copy(
                            data = result.data ,
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _removeVoteState.value =removeVoteState.value.copy(
                            data = result.data ,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}