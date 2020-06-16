package com.bicifiapp.statistics

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.statistics.datasource.StatisticsDataSource

class StatisticsRepositoryImp(
    private val dataSource: StatisticsDataSource,
    private val networkHandler: NetworkHandler
) : StatisticsRepository {

    override suspend fun getStatisticsTestByUser(userId: String): Either<Failure, Statistics> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSource.getTestStatistic(userId).toStatistics()
                )
                else -> Either.Left(Failure.NetworkConnection)
            }

        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }
}
