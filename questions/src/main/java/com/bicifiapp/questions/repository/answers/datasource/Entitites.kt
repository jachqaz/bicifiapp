package com.bicifiapp.questions.repository.answers.datasource

import com.bicifiapp.questions.repository.answers.Answer

data class AnswerEntity(
    val userId: String,
    val questionId: String,
    val response: String,
    val date: String
) {
    fun toAnswer() =
        Answer(
            userId,
            questionId,
            response,
            date
        )
}

data class HeadAnswer(
    val userId: String,
    val date: String,
    val answerEmotionalState: String
)

data class AnswersSimple(
    val questionId: String,
    val response: String
)