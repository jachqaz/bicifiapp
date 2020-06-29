package com.bicifiapp.statistics

data class Statistics(
    val statisticQuestions: List<TestStatistic>,
    val statisticEmotional: List<TestStatistic>
)

data class TestStatistic(
    val date: String,
    val level: Int?,
    val levelEmotional: Int
)
