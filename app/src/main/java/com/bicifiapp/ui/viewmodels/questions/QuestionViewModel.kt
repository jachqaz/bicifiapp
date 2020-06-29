package com.bicifiapp.ui.viewmodels.questions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.answers.Answer
import com.bicifiapp.questions.repository.answers.AnswersRepository
import com.bicifiapp.questions.repository.question.Question
import com.bicifiapp.questions.repository.question.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(
    private val questionRepository: QuestionRepository,
    private val answersRepository: AnswersRepository
) : ViewModel() {

    private lateinit var resultQuestions: Either<Failure, List<Question>>
    private lateinit var resultAnswers: Either<Failure, String>
    private lateinit var resultCalculateLevel: Either<Failure, Int>

    val resultQuestionsLiveData by lazy {
        MutableLiveData<State>()
    }

    val resultAnswersLiveData by lazy {
        MutableLiveData<State>()
    }

    val resultCalculateLevelLiveData by lazy {
        MutableLiveData<State>()
    }

    fun getQuestions(questionType: String, userId: String) {
        resultAnswersLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultQuestions = questionRepository.getQuestionsByType(questionType, userId)
            }
            resultQuestions.either(::handleFailureQuestions, ::getQuestionsSuccess)
        }
    }

    fun saveAnswers(answers: List<Answer>, emotionalState: String, questionType: String) {
        resultAnswersLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultAnswers = answersRepository.saveAnswers(answers, emotionalState, questionType)
            }
            resultAnswers.either(::handleFailureAnswers, ::getAnswersSuccess)
        }
    }

    fun calculateLevel(userId: String, answerId: String, emotionalState: String) {
        resultCalculateLevelLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultCalculateLevel =
                    answersRepository.calculateLevel(userId, answerId, emotionalState)
            }
            resultCalculateLevel.either(::handleFailureCalculate, ::setCalculateLevelSuccess)
        }
    }

    private fun getQuestionsSuccess(questions: List<Question>) {
        resultQuestionsLiveData.value = State.Success(questions)
    }

    private fun getAnswersSuccess(answerId: String) {
        resultAnswersLiveData.value = State.Success(answerId)
    }

    private fun setCalculateLevelSuccess(userLevel: Int) {
        resultCalculateLevelLiveData.value = State.Success(userLevel)
    }

    private fun handleFailureQuestions(failure: Failure) {
        resultQuestionsLiveData.value = State.Failed(failure)
    }

    private fun handleFailureAnswers(failure: Failure) {
        resultAnswersLiveData.value = State.Failed(failure)
    }

    private fun handleFailureCalculate(failure: Failure) {
        resultCalculateLevelLiveData.value = State.Failed(failure)
    }
}
