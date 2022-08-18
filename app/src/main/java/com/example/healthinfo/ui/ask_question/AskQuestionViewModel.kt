package com.example.healthinfo.ui.ask_question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthinfo.core.Resource
import com.example.healthinfo.data.remote.body_request.QuestionAskedBodyRequest
import com.example.healthinfo.domain.repository.AuthRepositoryInterface
import com.example.healthinfo.domain.repository.QuestionTitleRepository
import com.example.healthinfo.ui.main.TokenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AskQuestionViewModel
@Inject
constructor(
    private val questionTitleRepository: QuestionTitleRepository,
    private val authRepositoryInterface: AuthRepositoryInterface
):ViewModel() {
    private val _askQuestionState = MutableStateFlow(AskQuestionState())
    val askQuestionState = _askQuestionState.asStateFlow()
    private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
    private var searchJob: Job? = null
    fun askQuestion(email:String,img:String?, question_text:String, question_title:String) {
        searchJob = viewModelScope.launch(ioDispatchers) {
            delay(500L)
            questionTitleRepository.askQuestion(requestBody = QuestionAskedBodyRequest(email = email, img = img, text = question_text, title =  question_title))
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _askQuestionState.value = _askQuestionState.value.copy(
                                data = result.data,
                                isLoading = false
                            )
                        }
                        is Resource.Loading -> {
                            _askQuestionState.value = _askQuestionState.value.copy(
                                data = result.data,
                                isLoading = true
                            )
                        }
                        is Resource.Error -> {
                            _askQuestionState.value = _askQuestionState.value.copy(
                                data = result.data,
                                isLoading = false
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}