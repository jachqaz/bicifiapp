package com.bicifiapp.questions.repository.question.datasources

interface DataSourceQuestion {

    suspend fun getQuestionsByType(questionType: String, userId: String): List<QuestionEntity>
}
