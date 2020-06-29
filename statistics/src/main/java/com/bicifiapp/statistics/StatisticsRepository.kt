package com.bicifiapp.statistics

import co.devhack.base.Either
import co.devhack.base.error.Failure

interface StatisticsRepository {

    suspend fun getStatisticsTestByUser(userId: String): Either<Failure, Statistics>
}
