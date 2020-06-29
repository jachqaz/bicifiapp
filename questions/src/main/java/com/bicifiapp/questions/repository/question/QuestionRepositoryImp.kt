package com.bicifiapp.questions.repository.question

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.question.datasources.DataSourceQuestion

class QuestionRepositoryImp(
    private val dataSource: DataSourceQuestion,
    private val networkHandler: NetworkHandler
) : QuestionRepository {

    override suspend fun getQuestionsByType(
        questionType: String,
        userId: String
    ): Either<Failure, List<Question>> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSource.getQuestionsByType(questionType, userId).map {
                        it.toQuestion()
                    }
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }
}
