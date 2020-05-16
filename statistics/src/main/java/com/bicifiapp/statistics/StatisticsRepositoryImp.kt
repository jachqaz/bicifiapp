package com.bicifiapp.statistics

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.statistics.datasource.StatisticsDataSource

class StatisticsRepositoryImp(
    private val dataSource: StatisticsDataSource
) : StatisticsRepository {

    companion object {
        private const val TAKE_LAST = 10
    }

    override suspend fun getStatisticsTestByUser(userId: String): Either<Failure, List<TestStatistic>> =
        try {
            Either.Right(
                dataSource.getTestStatistic(userId).map {
                    it.toTestStatistic()
                }.sortedByDescending {
                    it.date
                }.reversed().take(TAKE_LAST)
            )
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }

}