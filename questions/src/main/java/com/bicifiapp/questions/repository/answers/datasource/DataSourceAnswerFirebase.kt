package com.bicifiapp.questions.repository.answers.datasource

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DataSourceAnswerFirebase : DataSourceAnswer {

    private companion object {
        const val ANSWER_COLLECTION = "answers"
        const val USER_ID_FIELD = "userId"
        const val ANSWER_ID_FIELD = "answerId"
        const val EMOTIONAL_STATE_FIELD = "emotionalState"
        const val FUNCTION_NAME_CALCULATE_LEVEL = "calculateLevel"
        const val FUNCTION_NAME_GET_CURRENT_LEVEL_BY_USER = "getCurrentLevelByUser"
        const val LAST_LEVEL_FIELD = "lastlevel"
        const val DATE_LAST_LEVEL_FIELD = "datelasttest"
        const val TITLE_LAST_LEVEL_FIELD = "titlelevel"
        const val DESCRIPTION_LAST_LEVEL_FIELD = "descriptionLevel"
        const val LEVEL_FIELD = "level"
    }

    private val db by lazy {
        Firebase.firestore
    }

    private val functions by lazy {
        Firebase.functions
    }

    override suspend fun saveAnswers(answers: List<AnswerEntity>, emotionalState: String): String =
        suspendCoroutine { continuation ->

            createHeadAnswer(
                HeadAnswer(
                    answers[0].userId,
                    answers[0].date,
                    emotionalState
                )
            ) { id ->
                answers.forEach { answer ->
                    db.collection(ANSWER_COLLECTION)
                        .document(id)
                        .collection(ANSWER_COLLECTION)
                        .add(
                            AnswersSimple(
                                answer.questionId,
                                answer.response
                            )
                        )
                        .addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
                }

                continuation.resume(id)
            }
        }

    override suspend fun calculateLevel(userId: String, answerId: String): Int =
        suspendCoroutine { continuation ->
            val data = hashMapOf(
                USER_ID_FIELD to userId,
                ANSWER_ID_FIELD to answerId,
                EMOTIONAL_STATE_FIELD to answerId
            )

            functions.getHttpsCallable(FUNCTION_NAME_CALCULATE_LEVEL)
                .call(data)
                .addOnSuccessListener { task -> continuation.resume((task.data as Map<*, *>)[LEVEL_FIELD] as Int) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

    override suspend fun getLastUserLevel(userId: String): LastUserLevelRecordEntity =
        suspendCoroutine { continuation ->
            val data = hashMapOf(
                USER_ID_FIELD to userId
            )

            functions.getHttpsCallable(FUNCTION_NAME_GET_CURRENT_LEVEL_BY_USER)
                .call(data)
                .addOnSuccessListener { task ->
                    val map = task.data as Map<*, *>
                    continuation.resume(
                        LastUserLevelRecordEntity(
                            map[LAST_LEVEL_FIELD] as Int,
                            map[DATE_LAST_LEVEL_FIELD] as String,
                            map[TITLE_LAST_LEVEL_FIELD] as String,
                            map[DESCRIPTION_LAST_LEVEL_FIELD] as String
                        )
                    )
                }
                .addOnFailureListener { continuation.resumeWithException(it) }

        }


    private fun createHeadAnswer(headAnswer: HeadAnswer, block: (String) -> Unit) =
        db.collection(ANSWER_COLLECTION)
            .add(headAnswer)
            .addOnSuccessListener {
                block(it.id)
            }.addOnFailureListener {
                throw it
            }

}
