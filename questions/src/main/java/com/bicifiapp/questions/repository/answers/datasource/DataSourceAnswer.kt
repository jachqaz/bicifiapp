package com.bicifiapp.questions.repository.answers.datasource

interface DataSourceAnswer {

    suspend fun saveAnswers(
        answers: List<AnswerEntity>,
        emotionalState: String,
        questionType: String
    ): String

    suspend fun calculateLevel(userId: String, answerId: String, emotionalState: String): Int

    suspend fun getLastUserLevel(userId: String): LastUserLevelRecordEntity

    suspend fun saveEmotionalState(emotionalState: String, userId: String): Boolean
}
