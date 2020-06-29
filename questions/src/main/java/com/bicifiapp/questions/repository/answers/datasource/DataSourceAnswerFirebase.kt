package com.bicifiapp.questions.repository.answers.datasource

import co.devhack.androidextensions.getDateWithFormat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DataSourceAnswerFirebase : DataSourceAnswer {

    private companion object {
        const val EMOTIONAL_STATE_COLLECTION = "emotionalstate"
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
        const val FORMAT_DATE = "dd-MM-yyyy HH:mm"
    }

    private val db by lazy {
        Firebase.firestore
    }

    private val functions by lazy {
        Firebase.functions
    }

    override suspend fun saveAnswers(
        answers: List<AnswerEntity>,
        emotionalState: String,
        questionType: String
    ): String {

        val answerHeaderId = createHeadAnswer(
            HeadAnswer(
                answers[0].userId,
                answers[0].date,
                emotionalState,
                questionType
            )
        )

        answers.map { answer ->
            AnswersSimple(
                answer.questionId,
                answer.response
            )
        }.forEach { answerSimple ->
            saveDetailAnswer(answerSimple, answerHeaderId)
        }

        return answerHeaderId
    }

    override suspend fun calculateLevel(
        userId: String,
        answerId: String,
        emotionalState: String
    ): Int =
        suspendCoroutine { continuation ->
            val data = hashMapOf(
                USER_ID_FIELD to userId,
                ANSWER_ID_FIELD to answerId,
                EMOTIONAL_STATE_FIELD to emotionalState
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

    override suspend fun saveEmotionalState(emotionalState: String, userId: String): Boolean =
        suspendCoroutine { continuation ->
            db.collection(EMOTIONAL_STATE_COLLECTION)
                .add(
                    EmotionalStateEntity(
                        Date().getDateWithFormat(FORMAT_DATE),
                        emotionalState,
                        userId
                    )
                )
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }.addOnSuccessListener {
                    continuation.resume(true)
                }
        }

    private suspend fun createHeadAnswer(headAnswer: HeadAnswer): String =
        suspendCoroutine { continuation ->
            db.collection(ANSWER_COLLECTION)
                .add(headAnswer)
                .addOnSuccessListener {
                    continuation.resume(it.id)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    private suspend fun saveDetailAnswer(
        answersSimple: AnswersSimple,
        idAnswerHeader: String
    ): Boolean = suspendCoroutine { continuation ->
        db.collection(ANSWER_COLLECTION)
            .document(idAnswerHeader)
            .collection(ANSWER_COLLECTION)
            .add(answersSimple)
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }
}
