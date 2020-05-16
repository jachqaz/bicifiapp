package com.bicifiapp.statistics.datasource

import com.bicifiapp.statistics.TestStatistic
import java.time.LocalDateTime

data class TestStatisticEntity(
    val date: LocalDateTime?,
    val level: Int
) {
    fun toTestStatistic() =
        TestStatistic(
            date,
            level
        )
}
