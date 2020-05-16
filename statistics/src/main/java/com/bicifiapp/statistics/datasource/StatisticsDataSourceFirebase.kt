package com.bicifiapp.statistics.datasource

import android.os.Build
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StatisticsDataSourceFirebase : StatisticsDataSource {

    private companion object {
        const val FUNCTION_GET_TEST_STATISTIC = "getTestStatisticsByUser"
        const val USER_ID_FIELD = "userId"
        const val DATE_FIELD = "date"
        const val LEVEL_FIELD = "level"
        const val SPLIT_DELIMITERS_DATE = "-"
        const val SPLIT_DELIMITERS_TIME = ":"
        const val SPLIT_DELIMITERS_EMPTY = " "
        private const val DATE_INDEX_ZERO = 0
        private const val DATE_INDEX_ONE = 1
        private const val DATE_INDEX_TWO = 2
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
                    val statistics = ArrayList<TestStatisticEntity>().apply {
                        val resp = task.data as ArrayList<HashMap<String, Any>>
                        resp.forEach { element ->
                            add(
                                TestStatisticEntity(
                                    getDateTime(element[DATE_FIELD].toString()),
                                    element[LEVEL_FIELD].toString().toInt()
                                )
                            )
                        }
                    }
                    continuation.resume(statistics)
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

    private fun getDateTime(date: String): LocalDateTime? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateTimeSplit = date.split(SPLIT_DELIMITERS_EMPTY)
            val dateSplit = dateTimeSplit[0].split(SPLIT_DELIMITERS_DATE)
            val timeSplit = dateTimeSplit[1].split(SPLIT_DELIMITERS_TIME)
            return LocalDateTime.of(
                dateSplit[DATE_INDEX_TWO].toInt(),
                dateSplit[DATE_INDEX_ONE].toInt(),
                dateSplit[DATE_INDEX_ZERO].toInt(),
                timeSplit[DATE_INDEX_ZERO].toInt(),
                timeSplit[DATE_INDEX_ONE].toInt()
            )
        }
        return null
    }

}