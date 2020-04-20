package com.bicifiapp.questions.repository.question.datasources

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DataSourceQuestionFirestore : DataSourceQuestion {

    private companion object {
        const val QUESTION_COLLECTION = "questions"
        const val ORDER_FIELD = "order"
        const val LABEL_FIELD = "label"
        const val TEXT_FIELD = "text"
    }

    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun getAllQuestions(): List<QuestionEntity> =
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

}
