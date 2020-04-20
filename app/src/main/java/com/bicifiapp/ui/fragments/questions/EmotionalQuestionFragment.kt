package com.bicifiapp.ui.fragments.questions

import android.content.Context
import android.os.Bundle
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentEmotionalQuestionBinding
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import timber.log.Timber

class EmotionalQuestionFragment : BaseFragment(R.layout.fragment_emotional_question) {

    private lateinit var callback: OnQuestionEmotionalListener
    private lateinit var dialogLoading: DialogLoading
    private var _binding: FragmentEmotionalQuestionBinding? = null
    private val binding get() = _binding!!

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView() {
        _binding = FragmentEmotionalQuestionBinding.bind(view!!)
        initListeners()
        loadDataUI()
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
        callback.onQuestionEmotionalResponse(emotionalState)
    }

    private fun loadDataUI() {
        arguments?.let {
            val dateLastResponse = it.getString(DATE_LAST_RESPONSE_ARG)
            binding.textdateLastQtEmotional.text =
                if (dateLastResponse!!.isEmpty()) getString(R.string.lbl_first_time) else dateLastResponse
        }
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
