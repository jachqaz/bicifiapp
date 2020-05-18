package com.bicifiapp.ui.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentHomeScreenBinding
import com.bicifiapp.extensions.*
import com.bicifiapp.questions.repository.answers.LastUserLevelRecord
import com.bicifiapp.ui.activity.questions.QuestionActivity
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.home.HomeViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val IS_FREE_USER = "isFreeUser"
private const val TEXT_LAST_LEVEL = "text_last_level"
private const val LAST_DATE_RESPONSE_QUESTIONS = "last_date_response"


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

    override fun initView(view: View) {
        _binding = FragmentHomeScreenBinding.bind(view)
        arguments?.let {
            isFreeUser = it.getBoolean(IS_FREE_USER)
        }
        initListener()
        initLiveData()
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

    override fun onResume() {
        super.onResume()
        loadDataUI()
    }

    private fun initListener() {
        binding.btnRepeatQuestions.setOnClickListener {
            callback.repeatTest()
        }

        binding.btnRepeatQuestions.setOnClickListener {
            startActivity(
                Intent(activity, QuestionActivity::class.java).apply {
                    putExtra(
                        LAST_DATE_RESPONSE_QUESTIONS,
                        getSharedPreferences()?.getString(LAST_DATE_TEST, String.empty())
                    )
                }
            )
        }
    }

    private fun initLiveData() {
        liveDataObserve(
            homeViewModel.getCurrentUserLevelLiveData,
            ::onGetCurrentUserLevelStateChange
        )
    }

    private fun loadDataUI() {
        val userId = userId()
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
        saveSharedPreferences(
            userLastLevel.titleLevel,
            userLastLevel.lastLevel,
            userLastLevel.dateLastLevel
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveSharedPreferences(txtLastLevel: String, lastLevel: Int, dateLastLevel: String) {
        claims { claims ->
            getSharedPreferences()?.edit {
                putString(TEXT_LAST_LEVEL, "$txtLastLevel - $lastLevel%")
                putString(LAST_EMOTIONAL_STATE, claims[LAST_EMOTIONAL_STATE])
                putString(
                    LAST_DATE_TEST,
                    dateLastLevel
                )
            }
        }

    }

    companion object {

        const val DATE_FORMAT = "dd-MM-yyyy HH:mm"

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
