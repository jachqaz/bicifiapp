package com.bicifiapp.ui.viewmodels.emotionalquestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.answers.AnswersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class EmotionalQuestionViewModel(
    private val answersRepository: AnswersRepository
) : ViewModel() {

    private lateinit var result: Either<Failure, Boolean>

    val saveEmotionalStateLiveData by lazy {
        MutableLiveData<State>()
    }

    fun saveEmotionalState(emotionalState: String) {
        saveEmotionalStateLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                result = answersRepository.saveEmotionalState(emotionalState)
            }
            result.either(::handleFailure, ::saveEmotionalStateSuccess)
        }
    }

    private fun saveEmotionalStateSuccess(success: Boolean) {
        saveEmotionalStateLiveData.value = State.Success(success)
    }

    private fun handleFailure(failure: Failure) {
        saveEmotionalStateLiveData.value = State.Failed(failure)
        Timber.e("error in EmotionalQuestionViewModel: $failure")
    }
}