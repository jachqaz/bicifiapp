package com.bicifiapp.ui.fragments.questions

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.content.edit
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.androidextensions.getDateWithFormat
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentEmotionalQuestionBinding
import com.bicifiapp.extensions.LAST_DATE_EMOTIONAL_TEST
import com.bicifiapp.extensions.LAST_DATE_TEST
import com.bicifiapp.extensions.LAST_EMOTIONAL_STATE
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.emotionalquestion.EmotionalQuestionViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class EmotionalQuestionFragment : BaseFragment(R.layout.fragment_emotional_question) {

    private val emotionalQuestionViewModel by inject<EmotionalQuestionViewModel>()

    private var callback: OnQuestionEmotionalListener? = null
    private lateinit var dialogLoading: DialogLoading
    private var _binding: FragmentEmotionalQuestionBinding? = null
    private val binding get() = _binding!!

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView(view: View) {
        _binding = FragmentEmotionalQuestionBinding.bind(view)
        initListeners()
        loadDataUI()
        initLiveData()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = activity as OnQuestionEmotionalListener
        } catch (ex: Exception) {
            Timber.e(
                "The activity must implemented OnQuestionEmotionalListener "
            )
        }
    }

    private fun initListeners() {
        binding.btnEmotionalVeryDissatisfied.setOnClickListener {
            sendResponseEmotionalState(getString(R.string.i_am_defeated))
        }

        binding.btnEmotionalDissatisfied.setOnClickListener {
            sendResponseEmotionalState(getString(R.string.i_am_soso))
        }

        binding.btnEmotionalSatisfied.setOnClickListener {
            sendResponseEmotionalState(getString(R.string.i_am_almost_fine))
        }

        binding.btnEmotionalVerySatisfied.setOnClickListener {
            sendResponseEmotionalState(getString(R.string.i_am_fine))
        }
    }

    private fun sendResponseEmotionalState(emotionalState: String) {
        when (callback) {
            null -> {
                getSharedPreferences()?.edit {
                    putString(
                        LAST_DATE_EMOTIONAL_TEST,
                        Date().getDateWithFormat(DATE_FORMAT)
                    )
                }
                emotionalQuestionViewModel.saveEmotionalState(emotionalState)
            }
            else -> callback?.onQuestionEmotionalResponse(emotionalState)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadDataUI() {
        binding.textdateLastQtEmotional.text =
            getSharedPreferences()?.getString(LAST_DATE_TEST, null)?.let {
                it
            } ?: run {
                getSharedPreferences()?.getString(LAST_EMOTIONAL_STATE, null)?.let {
                    it
                } ?: getString(R.string.lbl_first_time)
            }
    }


    private fun initLiveData() {
        liveDataObserve(
            emotionalQuestionViewModel.saveEmotionalStateLiveData,
            ::onArticleByLabelStateChange
        )
    }

    private fun onArticleByLabelStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                //handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                notifySuccess(
                    messageIdRes = R.string.lbl_success_emotional_state,
                    colorId = R.color.success_snackbar
                )
                hideProgress()
            }
            null -> hideProgress()
        }

    companion object {

        const val DATE_FORMAT = "dd-MM-yyyy HH:mm"
        @JvmStatic
        fun newInstance() = EmotionalQuestionFragment()
    }

    interface OnQuestionEmotionalListener {
        fun onQuestionEmotionalResponse(emotionalState: String)
    }
}
