package com.bicifiapp.ui.fragments.home

import android.content.Context
import android.os.Bundle
import androidx.core.content.edit
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentHomeScreenBinding
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.extensions.safeString
import com.bicifiapp.questions.repository.answers.LastUserLevelRecord
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val IS_FREE_USER = "isFreeUser"
private const val TEXT_LAST_LEVEL = "text_last_level"


class HomeScreenFragment : BaseFragment(R.layout.fragment_home_screen) {

    private val homeViewModel by inject<HomeViewModel>()

    private lateinit var callback: HomeListener
    private var isFreeUser: Boolean = false
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogLoading: DialogLoading

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView() {
        _binding = FragmentHomeScreenBinding.bind(view)
        arguments?.let {
            isFreeUser = it.getBoolean(IS_FREE_USER)
        }
        initListener()
        initLiveData()
        loadDataUI()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = activity as HomeListener
        } catch (ex: Exception) {
            Timber.e(
                "The activity must implemented HomeListener"
            )
        }
    }

    private fun initListener() {
        binding.btnRepeatQuestions.setOnClickListener {
            callback.repeatTest()
        }
    }

    private fun initLiveData() {
        liveDataObserve(
            homeViewModel.getCurrentUserLevelLiveData,
            ::onGetCurrentUserLevelStateChange
        )
    }

    private fun loadDataUI() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.safeString()
        homeViewModel.getCurrentUserLevel(userId)
    }

    private fun onGetCurrentUserLevelStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                //handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                setTextUserLastLevel(state.responseTo())
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun setTextUserLastLevel(userLastLevel: LastUserLevelRecord) {
        binding.txtLevel.text = userLastLevel.titleLevel
        binding.txtLastLevel.text = getString(R.string.lbl_last_level)
            .format(userLastLevel.lastLevel, userLastLevel.dateLastLevel)
        binding.txtResultLevel.text = userLastLevel.descriptionLevel
        saveSharedPreferences(userLastLevel.titleLevel, userLastLevel.lastLevel)
    }

    private fun saveSharedPreferences(txtLastLevel: String, lastLevel: Int) {
        getSharedPreferences()?.edit {
            putString(TEXT_LAST_LEVEL, "$txtLastLevel - $lastLevel%")
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(isPayUser: Boolean) =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_FREE_USER, isPayUser)
                }
            }
    }

    interface HomeListener {
        fun repeatTest()
    }
}
