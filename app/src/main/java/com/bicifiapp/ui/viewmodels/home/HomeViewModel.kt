package com.bicifiapp.ui.viewmodels.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.bicifiapp.notificationssettings.notification.Notification
import com.bicifiapp.notificationssettings.notification.NotificationRepository
import com.bicifiapp.questions.repository.answers.AnswersRepository
import com.bicifiapp.questions.repository.answers.LastUserLevelRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel(
    private val answersRepository: AnswersRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private lateinit var result: Either<Failure, LastUserLevelRecord>
    private lateinit var resultNotification: Either<Failure, Notification>
    private lateinit var resultSaveNotification: Either<Failure, Boolean>

    val getCurrentUserLevelLiveData by lazy {
        MutableLiveData<State>()
    }

    val getNotificationLiveData by lazy {
        MutableLiveData<State>()
    }

    val saveNotificationLiveData by lazy {
        MutableLiveData<State>()
    }

    fun getCurrentUserLevel(userId: String) {
        getCurrentUserLevelLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                result = answersRepository.getLastUserLevel(userId)
            }
            result.either(::handleFailure, ::handleGetLastUserLevelSuccess)
        }
    }

    fun saveNotification(userId: String, notification: Notification) {
        getCurrentUserLevelLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultSaveNotification = notificationRepository.save(userId, notification)
            }
            resultSaveNotification.either(
                ::handleFailureSaveNotification,
                ::handleSaveNotificationSuccess
            )
        }
    }

    fun getNotification(userId: String) {
        getNotificationLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultNotification = notificationRepository.getByUser(userId)
            }
            resultNotification.either(::handleFailureNotification, ::handleGetNotificationSuccess)
        }
    }

    private fun handleGetLastUserLevelSuccess(lastUserLevelRecord: LastUserLevelRecord) {
        getCurrentUserLevelLiveData.value = State.Success(lastUserLevelRecord)
    }

    private fun handleFailure(failure: Failure) {
        getCurrentUserLevelLiveData.value = State.Failed(failure)
        Timber.e("error in HomeViewModel: $failure")
    }

    private fun handleGetNotificationSuccess(notification: Notification) {
        getNotificationLiveData.value = State.Success(notification)
    }

    private fun handleFailureNotification(failure: Failure) {
        getNotificationLiveData.value = State.Failed(failure)
        Timber.e("error in HomeViewModel: $failure")
    }

    private fun handleSaveNotificationSuccess(success: Boolean) {
        saveNotificationLiveData.value = State.Success(success)
    }

    private fun handleFailureSaveNotification(failure: Failure) {
        saveNotificationLiveData.value = State.Failed(failure)
        Timber.e("error in HomeViewModel: $failure")
    }
}
