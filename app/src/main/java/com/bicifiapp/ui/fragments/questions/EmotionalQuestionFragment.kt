package com.bicifiapp.ui.fragments.questions

import android.content.Context
import android.os.Bundle
import android.view.View
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentEmotionalQuestionBinding
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.emotionalquestion.EmotionalQuestionViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

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
            null -> emotionalQuestionViewModel.saveEmotionalState(emotionalState)
            else -> callback?.onQuestionEmotionalResponse(emotionalState)
        }
    }

    private fun loadDataUI() {
        arguments?.let {
            val dateLastResponse = it.getString(DATE_LAST_RESPONSE_ARG)
            binding.textdateLastQtEmotional.text =
                if (dateLastResponse!!.isEmpty()) getString(R.string.lbl_first_time) else dateLastResponse
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
        private const val DATE_LAST_RESPONSE_ARG = "dateLastResponse"

        @JvmStatic
        fun newInstance(dateLastResponse: String) =
            EmotionalQuestionFragment().apply {
                arguments =
                    Bundle().apply {
                        putString(DATE_LAST_RESPONSE_ARG, dateLastResponse)
                    }
            }
    }

    interface OnQuestionEmotionalListener {
        fun onQuestionEmotionalResponse(emotionalState: String)
    }
}
