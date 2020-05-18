package com.bicifiapp.ui.fragments.profile

import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.androidextensions.ui.dialogDate
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentProfileBinding
import com.bicifiapp.extensions.activity
import com.bicifiapp.extensions.userId
import com.bicifiapp.notificationssettings.repository.Profile
import com.bicifiapp.ui.activity.questions.QuestionActivity
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val profileViewModel by inject<ProfileViewModel>()

    private lateinit var dialogLoading: DialogLoading
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView(view: View) {
        _binding = FragmentProfileBinding.bind(view)
        initListeners()
        initAdapterRolFamily()
        initAdapterEducationLevel()
        initLiveData()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    private fun initListeners() {
        binding.profileBirthday.dialogDate(activity())

        binding.switchLocalPass.setOnClickListener {
            requestPermissionEvent()
        }

        binding.btnStartTest.setOnClickListener {
            saveProfileData()
        }
    }

    private fun initAdapterEducationLevel() {
        ArrayAdapter.createFromResource(
            activity(),
            R.array.education_level_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.educationLevel.adapter = adapter
            binding.educationLevel.setSelection(0)
        }
    }

    private fun initAdapterRolFamily() {
        ArrayAdapter.createFromResource(
            activity(),
            R.array.rol_family_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.familyRol.adapter = adapter
            binding.familyRol.setSelection(0)
        }
    }

    private fun requestPermissionEvent() {
        if (binding.switchLocalPass.isChecked) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                CODE_REQUEST_PERMISSION_LOCATION
            ) {

            }
        }
    }

    private fun saveProfileData() {
        val birthday = binding.profileBirthday.text.toString()
        val familyRol = binding.familyRol.selectedItem.toString()
        val educationLevel = binding.educationLevel.selectedItem.toString()
        val yearExperience = binding.yearExperience.text.toString().toInt()
        val acceptNotification = binding.switchNotificationPass.isChecked
        val acceptLocation = binding.switchLocalPass.isChecked

        when {
            birthday.isEmpty() -> {
                showSnackBar(R.string.error_profile_no_birthday, R.color.error_snackbar)
            }
            familyRol.isEmpty() -> {
                showSnackBar(R.string.error_profile_no_familyrol, R.color.error_snackbar)
            }
            educationLevel.isEmpty() -> {
                showSnackBar(R.string.error_profile_no_educationlevel, R.color.error_snackbar)
            }
            yearExperience <= 0 -> {
                showSnackBar(R.string.error_profile_no_yearexperience, R.color.error_snackbar)
            }
            else -> {
                profileViewModel.saveProfile(
                    Profile(
                        userId(),
                        birthday,
                        familyRol,
                        educationLevel,
                        yearExperience,
                        acceptNotification,
                        acceptLocation
                    )
                )
            }
        }
    }

    private fun initLiveData() {
        liveDataObserve(profileViewModel.saveProfileLiveData, ::onSaveProfileStateChange)
    }

    private fun onSaveProfileStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                successful()
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun successful() {
        Intent(activity(), QuestionActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun showSnackBar(resId: Int, colorId: Int) = view?.rootView?.let {
        Snackbar.make(it, resId, Snackbar.LENGTH_SHORT).apply {
            view.setBackgroundColor(ContextCompat.getColor(context, colorId))
            show()
        }
    }

    companion object {
        const val CODE_REQUEST_PERMISSION_LOCATION: Int = 100
    }
}
