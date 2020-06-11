package com.bicifiapp.ui.viewmodels.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.answers.AnswersRepository
import com.bicifiapp.questions.repository.answers.LastUserLevelRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel(
    private val answersRepository: AnswersRepository
) : ViewModel() {

    private lateinit var result: Either<Failure, LastUserLevelRecord>

    val getCurrentUserLevelLiveData by lazy {
        MutableLiveData<State>()
    }

    fun getCurrentUserLevel(userId: String) {
        getCurrentUserLevelLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                result = answersRepository.getLastUserLevel(userId)
            }
            result.either(::handleFailure, ::hadleGetLastUserLevelSuccess)
        }
    }

    private fun hadleGetLastUserLevelSuccess(lastUserLevelRecord: LastUserLevelRecord) {
        getCurrentUserLevelLiveData.value = State.Success(lastUserLevelRecord)
    }

    private fun handleFailure(failure: Failure) {
        getCurrentUserLevelLiveData.value = State.Failed(failure)
        Timber.e("error in HomeViewModel: $failure")
    }
}
