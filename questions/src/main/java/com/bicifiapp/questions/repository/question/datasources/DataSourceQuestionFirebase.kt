package com.bicifiapp.questions.repository.question.datasources

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("UNCHECKED_CAST")
class DataSourceQuestionFirebase : DataSourceQuestion {

    private companion object {
        const val QUESTION_COLLECTION = "questions"
        const val ANSWERS_SUMMARY_COLLECTION = "answerssummary"
        const val ORDER_FIELD = "order"
        const val LABEL_FIELD = "label"
        const val TEXT_FIELD = "text"
        const val QUESTION_FAILED_FIELD = "questionfailed"
        const val QUESTION_ALL_TYPE = "A"
    }

    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun getQuestionsByType(
        questionType: String,
        userId: String
    ): List<QuestionEntity> =
        when (questionType) {
            QUESTION_ALL_TYPE -> getAllQuestions()
            else -> getQuestionsFailed(userId)
        }

    private suspend fun getAllQuestions(): List<QuestionEntity> =
        suspendCoroutine { continuation ->
            db.collection(QUESTION_COLLECTION)
                .orderBy(ORDER_FIELD)
                .get()
                .addOnSuccessListener { queryDocuments ->
                    continuation.resume(ArrayList<QuestionEntity>().also {
                        queryDocuments.forEach { document ->
                            it.add(
                                QuestionEntity(
                                    document.id,
                                    document.get(ORDER_FIELD).toString().toInt(),
                                    document.getString(TEXT_FIELD) ?: "",
                                    document.getString(LABEL_FIELD) ?: ""
                                )
                            )
                        }
                    })
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    private suspend fun getQuestionsFailed(userId: String): List<QuestionEntity> {
        val questionsIdsFailed = getQuestionIdsFailed(userId)
        val questions = getAllQuestions()
        return mutableListOf<QuestionEntity>().apply {
            questionsIdsFailed.forEach { questionId ->
                add(questions.first { it.id == questionId })
            }
        }
    }

    private suspend fun getQuestionIdsFailed(userId: String): List<String> =
        suspendCoroutine { continuation ->
            db.collection(ANSWERS_SUMMARY_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    documentSnapshot.get(QUESTION_FAILED_FIELD)?.let {
                        continuation.resume(documentSnapshot.get(QUESTION_FAILED_FIELD) as List<String>)
                    } ?: emptyList<String>()
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
}
