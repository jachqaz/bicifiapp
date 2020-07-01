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
    private lateinit var resultGetProfile: Either<Failure, Profile?>

    val saveProfileLiveData by lazy {
        MutableLiveData<State>()
    }

    val getProfileLiveData by lazy {
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

    fun getProfileByUserId(userId: String) {
        getProfileLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultGetProfile = profileRepository.getByUserId(userId)
            }
            resultGetProfile.either(::getProfileHandleFailure, ::getProfileSuccess)
        }
    }

    private fun saveProfileSuccess(success: Boolean) {
        saveProfileLiveData.value = State.Success(success)
    }

    private fun handleFailure(failure: Failure) {
        saveProfileLiveData.value = State.Failed(failure)
        Timber.e("error in ProfileViewModel: $failure")
    }

    private fun getProfileSuccess(profile: Profile?) {
        if (profile == null) {
            getProfileLiveData.value = State.Failed(Failure.CustomError)
        } else {
            getProfileLiveData.value = State.Success(profile)
        }
    }

    private fun getProfileHandleFailure(failure: Failure) {
        getProfileLiveData.value = State.Failed(failure)
        Timber.e("error getting profile at ProfileViewModel: $failure")
    }
}
