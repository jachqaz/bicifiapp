package com.bicifiapp.statistics

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.statistics.datasource.StatisticsDataSource

class StatisticsRepositoryImp(
    private val dataSource: StatisticsDataSource
) : StatisticsRepository {

    override suspend fun getStatisticsTestByUser(userId: String): Either<Failure, List<TestStatistic>> =
        try {
            Either.Right(
                dataSource.getTestStatistic(userId).map {
                    it.toTestStatistic()
                }
            )
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }

}