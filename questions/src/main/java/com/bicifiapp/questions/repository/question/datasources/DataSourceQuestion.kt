package com.bicifiapp.questions.repository.question.datasources

interface DataSourceQuestion {

    suspend fun getAllQuestions(): List<QuestionEntity>

}
