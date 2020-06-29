package com.bicifiapp.statistics.datasource

import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("UNCHECKED_CAST")
class StatisticsDataSourceFirebase : StatisticsDataSource {

    private companion object {
        const val FUNCTION_GET_TEST_STATISTIC = "getTestStatisticsByUser"
        const val USER_ID_FIELD = "userId"
        const val DATE_FIELD = "date"
        const val LEVEL_FIELD = "level"
        const val LEVEL_EMOTIONAL_STATE_FIELD = "levelemotionalstate"
        const val STATISTICS_QUESTION_FIELD = "statisticsQuestions"
        const val STATISTICS_EMOTIONAL_FIELD = "statisticsEmotional"
    }

    private val functions by lazy {
        Firebase.functions
    }

    override suspend fun getTestStatistic(userId: String): StatisticsEntity =
        suspendCoroutine { continuation ->

            val data = hashMapOf(
                USER_ID_FIELD to userId
            )

            functions.getHttpsCallable(FUNCTION_GET_TEST_STATISTIC)
                .call(data)
                .addOnSuccessListener { task ->
                    try {
                        val resp = task.data as HashMap<String, Any>
                        val statisticQuestionsTmp =
                            resp[STATISTICS_QUESTION_FIELD] as List<HashMap<String, Any>>
                        val statisticEmotionalTmp =
                            resp[STATISTICS_EMOTIONAL_FIELD] as List<HashMap<String, Any>>

                        val statisticQuestions = ArrayList<TestStatisticEntity>().apply {
                            statisticQuestionsTmp.forEach { element ->
                                add(
                                    TestStatisticEntity(
                                        date = element[DATE_FIELD].toString(),
                                        level = element[LEVEL_FIELD].toString().toInt(),
                                        levelEmotional = element[LEVEL_EMOTIONAL_STATE_FIELD].toString()
                                            .toInt()
                                    )
                                )
                            }
                        }

                        val statisticEmotional = ArrayList<TestStatisticEntity>().apply {
                            statisticEmotionalTmp.forEach { element ->
                                add(
                                    TestStatisticEntity(
                                        date = element[DATE_FIELD].toString(),
                                        levelEmotional = element[LEVEL_EMOTIONAL_STATE_FIELD].toString()
                                            .toInt()
                                    )
                                )
                            }
                        }
                        continuation.resume(
                            StatisticsEntity(
                                statisticQuestions,
                                statisticEmotional
                            )
                        )
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
}
