package com.bicifiapp.statistics.datasource

import com.bicifiapp.statistics.TestStatistic

data class TestStatisticEntity(
    val date: String,
    val level: Int
) {
    fun toTestStatistic() =
        TestStatistic(
            date,
            level
        )
}
