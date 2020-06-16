package com.bicifiapp.questions.repository.answers

import co.devhack.base.Either
import co.devhack.base.error.Failure

interface AnswersRepository {

    suspend fun saveAnswers(
        answers: List<Answer>,
        emotionalState: String,
        questionType: String
    ): Either<Failure, String>

    suspend fun calculateLevel(
        userId: String,
        answerId: String,
        emotionalState: String
    ): Either<Failure, Int>

    suspend fun getLastUserLevel(userId: String): Either<Failure, LastUserLevelRecord>

    suspend fun saveEmotionalState(emotionalState: String, userId: String): Either<Failure, Boolean>
}
