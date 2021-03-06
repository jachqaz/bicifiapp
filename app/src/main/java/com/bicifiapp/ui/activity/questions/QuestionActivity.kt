package com.bicifiapp.ui.activity.questions

import android.content.Intent
import android.widget.Toast
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.androidextensions.getDateWithFormat
import co.devhack.base.State
import co.devhack.presentation.BaseActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.bicifiapp.R
import com.bicifiapp.databinding.ActivityQuestionBinding
import com.bicifiapp.extensions.empty
import com.bicifiapp.extensions.safeString
import com.bicifiapp.extensions.userId
import com.bicifiapp.questions.repository.answers.Answer
import com.bicifiapp.questions.repository.question.Question
import com.bicifiapp.ui.activity.homescreen.HomeScreenActivity
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.fragments.home.QuestionTypeEnum
import com.bicifiapp.ui.fragments.home.REPEAT_QUESTION_TYPE
import com.bicifiapp.ui.fragments.questions.EmotionalQuestionFragment
import com.bicifiapp.ui.fragments.questions.QuestionFragment
import com.bicifiapp.ui.viewmodels.questions.QuestionViewModel
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

const val LAST_DATE_RESPONSE_QUESTIONS = "last_date_response"

class QuestionActivity : BaseActivity(), QuestionFragment.OnQuestionListener,
    EmotionalQuestionFragment.OnQuestionEmotionalListener {

    private companion object {
        const val FORMAT_DATE = "dd-MM-yyyy HH:mm"
    }

    private val questionViewModel by inject<QuestionViewModel>()
    private lateinit var binding: ActivityQuestionBinding
    private lateinit var dialogLoading: DialogLoading
    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var lastDateResponseQuestions: String? = ""
    private lateinit var questionType: QuestionTypeEnum
    private var emotionalState = String.empty()
    private lateinit var userId: String
    private lateinit var answers: ArrayList<Answer>

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView() {
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = userId().safeString()
        initLiveData()
        loadDataIntent()
        loadQuestions()
        answers = ArrayList()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun onQuestionResponse(response: String, questionId: String) {
        answers.add(
            Answer(
                userId = userId,
                questionId = questionId,
                response = response,
                date = Date().getDateWithFormat(FORMAT_DATE)
            )
        )

        if (currentQuestionIndex == questions.size) {
            loadEmotionalFragment()
        } else {
            runQuestionFragment()
        }
    }

    override fun onQuestionEmotionalResponse(emotionalState: String) {
        this.emotionalState = emotionalState
        questionViewModel.saveAnswers(answers, emotionalState, questionType.code)
    }

    private fun loadDataIntent() {
        lastDateResponseQuestions = intent?.getStringExtra(LAST_DATE_RESPONSE_QUESTIONS)
        intent?.let {
            questionType =
                QuestionTypeEnum.valueOf(it.getStringExtra(REPEAT_QUESTION_TYPE) ?: "")
        }
    }

    private fun initLiveData() {
        liveDataObserve(questionViewModel.resultQuestionsLiveData, ::onGetQuestionsStateChange)
        liveDataObserve(questionViewModel.resultAnswersLiveData, ::onGetAnswersStateChange)
        liveDataObserve(
            questionViewModel.resultCalculateLevelLiveData,
            ::onCalculateLevelStateChange
        )
    }

    private fun loadQuestions() {
        questionViewModel.getQuestions(questionType.code, userId)
    }

    private fun runQuestionFragment() {
        binding.progressStatusQuestion.progress = currentQuestionIndex + 1

        val currentQuestion = questions[currentQuestionIndex]
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        transaction.replace(
            R.id.questionFrame, QuestionFragment.newInstance(
                currentQuestion.id,
                questions.size,
                currentQuestionIndex,
                lastDateResponseQuestions ?: String.empty(),
                currentQuestion.text,
                currentQuestion.label
            )
        )

        transaction.commit()

        currentQuestionIndex++
    }

    private fun loadEmotionalFragment() {
        binding.progressStatusQuestion.progress = currentQuestionIndex + 1
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        transaction.replace(R.id.questionFrame, EmotionalQuestionFragment.newInstance())
        transaction.commit()
    }

    private fun onGetQuestionsStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                this.questions = state.responseTo()
                if (hasQuestions(this.questions)) {
                    binding.progressStatusQuestion.max = this.questions.size + 1
                    runQuestionFragment()
                }
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun hasQuestions(questions: List<Question>) =
        when (questions.isEmpty()) {
            true -> {
                MaterialDialog(this).show {
                    title(R.string.lbl_congratulations)
                    message(R.string.lbl_message_dont_have_repeat_question)
                    positiveButton(R.string.lbl_ok) {
                        finish()
                    }
                }
                false
            }
            else -> true
        }

    private fun onGetAnswersStateChange(
        state: State?
    ) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                hideProgress()
                calculateUserLevel(state.responseTo())
            }
            null -> hideProgress()
        }

    private fun onCalculateLevelStateChange(
        state: State?
    ) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                hideProgress()
                calculateLevelSuccess(state.responseTo())
            }
            null -> hideProgress()
        }

    private fun calculateUserLevel(answerId: String) {
        questionViewModel.calculateLevel(userId, answerId, this.emotionalState)
    }

    private fun calculateLevelSuccess(userLevel: Int) {
        lastDateResponseQuestions?.let {
            finish()
        } ?: run {
            Toast.makeText(this, "el nivel es de $userLevel", Toast.LENGTH_LONG).show()
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
