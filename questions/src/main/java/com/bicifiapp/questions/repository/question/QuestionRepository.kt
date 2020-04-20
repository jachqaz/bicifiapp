package com.bicifiapp.questions.repository.question

import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.question.Question

interface QuestionRepository {

    suspend fun getAllQuestions(): Either<Failure, List<Question>>
}
