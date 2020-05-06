package com.bicifiapp.questions.repository.answers

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.answers.datasource.AnswerEntity
import com.bicifiapp.questions.repository.answers.datasource.DataSourceAnswer

class AnswersRepositoryImp(
    private val dataSource: DataSourceAnswer,
    private val networkHandler: NetworkHandler
) : AnswersRepository {

    override suspend fun saveAnswers(
        answers: List<Answer>,
        emotionalState: String
    ): Either<Failure, String> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSource.saveAnswers(
                        answers.map {
                            AnswerEntity(
                                it.userId,
                                it.questionId,
                                it.response,
                                it.date
                            )
                        },
                        emotionalState
                    )
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }

    override suspend fun calculateLevel(userId: String, answerId: String) =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSource.calculateLevel(userId, answerId)
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }

    override suspend fun getLastUserLevel(userId: String): Either<Failure, LastUserLevelRecord> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSource.getLastUserLevel(userId).toLastUserLevelRecord()
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }

}
