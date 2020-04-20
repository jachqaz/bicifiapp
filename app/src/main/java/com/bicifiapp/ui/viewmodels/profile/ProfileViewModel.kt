package com.bicifiapp.ui.viewmodels.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.bicifiapp.notificationssettings.repository.Profile
import com.bicifiapp.notificationssettings.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private lateinit var result: Either<Failure, Boolean>

    val saveProfileLiveData by lazy {
        MutableLiveData<State>()
    }

    fun saveProfile(profile: Profile) {
        saveProfileLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                result = profileRepository.save(profile)
            }
            result.either(::handleFailure, ::saveProfileSuccess)
        }
    }

    private fun saveProfileSuccess(success: Boolean) {
        saveProfileLiveData.value = State.Success(success)
    }

    private fun handleFailure(failure: Failure) {
        saveProfileLiveData.value = State.Failed(failure)
        Timber.e("error in ProfileViewModel: $failure")
    }

}