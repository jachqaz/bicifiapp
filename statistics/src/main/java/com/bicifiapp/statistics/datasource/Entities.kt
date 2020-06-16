package com.bicifiapp.statistics.datasource

import com.bicifiapp.statistics.Statistics
import com.bicifiapp.statistics.TestStatistic

data class StatisticsEntity(
    val statisticQuestions: List<TestStatisticEntity>,
    val statisticEmotional: List<TestStatisticEntity>
) {
    fun toStatistics() =
        Statistics(
            statisticQuestions.map {
                it.toTestStatistic()
            },
            statisticEmotional.map {
                it.toTestStatistic()
            }
        )
}

data class TestStatisticEntity(
    val date: String,
    val level: Int? = 0,
    val levelEmotional: Int

) {
    fun toTestStatistic() =
        TestStatistic(
            date,
            level,
            levelEmotional
        )
}
