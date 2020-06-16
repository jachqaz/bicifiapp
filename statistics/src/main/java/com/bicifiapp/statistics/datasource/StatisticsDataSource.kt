package com.bicifiapp.statistics.datasource

interface StatisticsDataSource {

    suspend fun getTestStatistic(userId: String): StatisticsEntity
}
