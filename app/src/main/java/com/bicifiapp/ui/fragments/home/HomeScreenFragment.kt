package com.bicifiapp.ui.fragments.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.checkItem
import com.afollestad.materialdialogs.list.checkItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentHomeScreenBinding
import com.bicifiapp.extensions.LAST_DATE_TEST
import com.bicifiapp.extensions.LAST_EMOTIONAL_STATE
import com.bicifiapp.extensions.activity
import com.bicifiapp.extensions.claims
import com.bicifiapp.extensions.empty
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.extensions.userId
import com.bicifiapp.notificationssettings.notification.Notification
import com.bicifiapp.questions.repository.answers.LastUserLevelRecord
import com.bicifiapp.ui.activity.questions.QuestionActivity
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.home.HomeViewModel
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

private const val IS_FREE_USER = "isFreeUser"
private const val TEXT_LAST_LEVEL = "text_last_level"
const val LAST_DATE_RESPONSE_QUESTIONS = "last_date_response"
const val REPEAT_QUESTION_TYPE = "repeat_question_type"

enum class QuestionTypeEnum(val code: String) {
    REPEAT_ALL("A"),
    REPEAT_FAILED("F")
}

class HomeScreenFragment : BaseFragment(R.layout.fragment_home_screen) {

    private val homeViewModel by inject<HomeViewModel>()

    private var isFreeUser: Boolean = false
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogLoading: DialogLoading
    private val daysSelected by lazy {
        mutableListOf<Int>()
    }
    private var hourSelected = ""

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

    override fun onResume() {
        super.onResume()
        loadDataUI()
    }

    private fun initListener() {
        binding.btnRepeatQuestions.setOnClickListener {
            startActivityQuestion(QuestionTypeEnum.REPEAT_ALL)
        }
        binding.btnShare.setOnClickListener { shareApp() }

        binding.btnRemember.setOnClickListener {
            homeViewModel.getNotification(userId())
        }
        binding.btnRepeatQuestionsFailed.setOnClickListener {
            startActivityQuestion(QuestionTypeEnum.REPEAT_FAILED)
        }
    }

    private fun showDaysNotification() {
        val dialog = MaterialDialog(activity()).show {
            title(R.string.select_day_weak)
            listItemsMultiChoice(R.array.days_array) { _, indices, _ ->
                daysSelected.clear()
                indices.forEach {
                    daysSelected.add(it)
                }
            }
            positiveButton(R.string.select) {
                showHoursNotification()
                dismiss()
            }
            negativeButton(R.string.cancel)
        }

        if (daysSelected.isNotEmpty()) {
            if (daysSelected.isNotEmpty())
                dialog.checkItems(daysSelected.toIntArray())
        }
    }

    private fun showHoursNotification() {
        val dialog = MaterialDialog(activity()).show {
            title(R.string.select_day_weak)
            listItemsSingleChoice(R.array.hours_array) { _, _, text ->
                hourSelected = text.toString()
                homeViewModel.saveNotification(
                    userId(), Notification(
                        daysSelected,
                        hourSelected
                    )
                )
            }
            positiveButton(R.string.select) {
                dismiss()
            }
            negativeButton(R.string.cancel)
        }

        getItemHour()?.let {
            dialog.checkItem(it)
        }
    }

    private fun getItemHour(): Int? {
        var indexTmp: Int? = null
        if (hourSelected != "") {
            resources.getStringArray(R.array.hours_array).forEachIndexed { index, hour ->
                if (hour == hourSelected) {
                    indexTmp = index
                }
            }
        }
        return indexTmp
    }

    private fun startActivityQuestion(repeatQuestionType: QuestionTypeEnum) =
        startActivity(
            Intent(
                activity, QuestionActivity::
                class.java
            ).apply
            {
                putExtra(
                    LAST_DATE_RESPONSE_QUESTIONS,
                    getSharedPreferences()?.getString(LAST_DATE_TEST, String.empty())
                )
                putExtra(
                    REPEAT_QUESTION_TYPE,
                    repeatQuestionType.name
                )
            }
        )

    private fun initLiveData() {
        liveDataObserve(
            homeViewModel.getCurrentUserLevelLiveData,
            ::onGetCurrentUserLevelStateChange
        )
        liveDataObserve(
            homeViewModel.getNotificationLiveData,
            ::onGetNotificationStateChange
        )
        liveDataObserve(
            homeViewModel.saveNotificationLiveData,
            ::onSaveNotificationStateChange
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
                // handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                setTextUserLastLevel(state.responseTo())
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun onGetNotificationStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                // handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                val notification = state.responseTo() as Notification
                notification.let {
                    daysSelected.clear()
                    daysSelected.addAll(it.days)
                    hourSelected = it.hour
                }
                hideProgress()
                showDaysNotification()
            }
            null -> hideProgress()
        }

    private fun onSaveNotificationStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                // handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                createScheduleNotification()
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun createScheduleNotification() {
        /*val saveRequest =
            PeriodicWorkRequestBuilder<RemainderWorker>(1, TimeUnit.HOURS)
                .build()

        WorkManager.getInstance(activity())
            .enqueue(saveRequest)*/
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

    private fun shareApp() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.lbl_share_message_default)
            )
            type = TEXT_PLAIN
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.lbl_subject))
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
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

        const val TEXT_PLAIN = "text/plain"

        @JvmStatic
        fun newInstance(isPayUser: Boolean) =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_FREE_USER, isPayUser)
                }
            }
    }
}
