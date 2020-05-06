package com.bicifiapp.statistics.datasource

import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StatisticsDataSourceFirebase : StatisticsDataSource {

    private companion object {
        const val FUNCTION_GET_TEST_STATISTIC = "getTestStatisticsByUser"
        const val USER_ID_FIELD = "userId"
        const val DATE_FIELD = "date"
        const val LEVEL_FIELD = "level"
    }

    private val functions by lazy {
        Firebase.functions
    }

    override suspend fun getTestStatistic(userId: String): List<TestStatisticEntity> =
        suspendCoroutine { continuation ->

            val data = hashMapOf(
                USER_ID_FIELD to userId
            )

            functions.getHttpsCallable(FUNCTION_GET_TEST_STATISTIC)
                .call(data)
                .addOnSuccessListener { task ->
                    ArrayList<TestStatisticEntity>().apply {
                        val resp = task.data as List<*>
                        resp.forEach {
                            add(
                                TestStatisticEntity(
                                    "it[LEVEL_FIELD]",
                                    2
                                )
                            )
                        }
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }

        }

}