package com.bicifiapp.questions.repository.answers.datasource

import com.bicifiapp.questions.repository.answers.Answer
import com.bicifiapp.questions.repository.answers.LastUserLevelRecord

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
    val answerEmotionalState: String,
    val type: String
)

data class AnswersSimple(
    val questionId: String,
    val response: String
)

data class LastUserLevelRecordEntity(
    val lastLevel: Int,
    val dateLastLevel: String,
    val titleLevel: String,
    val descriptionLevel: String
) {
    fun toLastUserLevelRecord() =
        LastUserLevelRecord(
            lastLevel,
            dateLastLevel,
            titleLevel,
            descriptionLevel
        )
}

data class EmotionalStateEntity(
    val date: String,
    val emotionalState: String,
    val userId: String
)
