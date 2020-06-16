package com.bicifiapp.questions.repository.question

import co.devhack.base.Either
import co.devhack.base.error.Failure

interface QuestionRepository {

    suspend fun getQuestionsByType(
        questionType: String,
        userId: String
    ): Either<Failure, List<Question>>
}
