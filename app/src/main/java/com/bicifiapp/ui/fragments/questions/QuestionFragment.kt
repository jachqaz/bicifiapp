package com.bicifiapp.ui.fragments.questions

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentQuestionBinding
import com.bicifiapp.extensions.safeString
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.questions.ArticleViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber


class QuestionFragment : BaseFragment(R.layout.fragment_question) {

    private val articleViewModel by inject<ArticleViewModel>()

    private lateinit var callback: OnQuestionListener
    private var _binding: FragmentQuestionBinding? = null
    private lateinit var dialogLoading: DialogLoading
    private val binding get() = _binding!!

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView() {
        _binding = FragmentQuestionBinding.bind(view!!)
        initListeners()
        initLiveData()
        loadDataUI()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = activity as OnQuestionListener
        } catch (ex: Exception) {
            Timber.e(
                "The activity must implemented OnQuestionListener "
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initLiveData() {
        liveDataObserve(
            articleViewModel.resultArticleKnowledgeLiveData,
            ::onArticleByLabelStateChange
        )
    }

    private fun initListeners() {
        binding.lblExtraInformation.setOnClickListener {
            if (binding.textExtraDescription.visibility != View.VISIBLE) {
                binding.textExtraDescription.visibility = View.VISIBLE
            } else {
                binding.textExtraDescription.visibility = View.GONE
            }
        }

        binding.btnQuestionNO.setOnClickListener {
            sendAnswer(NO_ANSWER)
        }

        binding.btnQuestionYES.setOnClickListener {
            sendAnswer(YES_ANSWER)
        }

        binding.lblExtraInformation.setOnClickListener {
            getArticle()
        }

    }

    private fun getArticle() {
        arguments?.let {
            articleViewModel.getArticleByLabel(it.getString(QUESTION_LABEL_ARG).safeString())
        }
    }

    private fun sendAnswer(response: String) {
        arguments?.let {
            callback.onQuestionResponse(response, it.getString(QUESTION_ID_ARG).safeString())
        }
    }

    private fun loadDataUI() {
        arguments?.let {
            val numeration = "${it.getInt(QUESTION_NUMBER_ARG) + 1} / ${it.getInt(
                TOTAL_QUESTIONS_ARG
            )}"

            binding.numerationQt.text = numeration

            val dateLastResponse = it.getString(DATE_LAST_RESPONSE_ARG)
            binding.textdateLastQt.text =
                if (dateLastResponse!!.isEmpty()) getString(R.string.lbl_first_time) else dateLastResponse

            binding.textDescriptionQuestion.text = it.getString(QUESTION_TEXT_ARG)

            val lblExtraInformation =
                SpannableString(getString(R.string.lbl_explain_database_knowledge))
            lblExtraInformation.setSpan(UnderlineSpan(), 0, lblExtraInformation.length, 0)
            binding.lblExtraInformation.text = lblExtraInformation
        }
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
                setTextDescriptionKnowledge(state.responseTo())
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun setTextDescriptionKnowledge(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.textExtraDescription.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.textExtraDescription.text = Html.fromHtml(text)
        }
    }

    companion object {

        private const val TOTAL_QUESTIONS_ARG = "totalQuestion"
        private const val QUESTION_NUMBER_ARG = "questionNumber"
        private const val DATE_LAST_RESPONSE_ARG = "dateLastResponse"
        private const val QUESTION_TEXT_ARG = "questionText"
        private const val QUESTION_LABEL_ARG = "questionLabel"
        private const val QUESTION_ID_ARG = "questionId"

        private const val YES_ANSWER = "YES"
        private const val NO_ANSWER = "NO"

        @JvmStatic
        fun newInstance(
            questionId: String,
            totalQuestions: Int,
            questionNumber: Int,
            dateLastResponse: String,
            questionText: String,
            questionLabel: String
        ) = QuestionFragment().apply {
            arguments =
                Bundle().apply {
                    putString(QUESTION_ID_ARG, questionId)
                    putInt(TOTAL_QUESTIONS_ARG, totalQuestions)
                    putInt(QUESTION_NUMBER_ARG, questionNumber)
                    putString(DATE_LAST_RESPONSE_ARG, dateLastResponse)
                    putString(QUESTION_TEXT_ARG, questionText)
                    putString(QUESTION_LABEL_ARG, questionLabel)
                }
        }
    }

    interface OnQuestionListener {
        fun onQuestionResponse(response: String, questionId: String)
    }
}

