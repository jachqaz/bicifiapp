package com.bicifiapp.notificationssettings.notification.datasources

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("UNCHECKED_CAST")
class NotificationFirestoreDataSource : NotificationDataSource {

    private companion object {
        const val NOTIFICATION_COLLECTION = "notification"
        const val DAYS_FIELDS = "days"
        const val HOUR_FIELD = "hour"
        const val STRING_EMPTY = ""
    }

    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun save(notification: NotificationEntity, userId: String): Boolean =
        suspendCoroutine { continuation ->
            db.collection(NOTIFICATION_COLLECTION)
                .document(userId)
                .set(
                    mapOf(
                        "days" to notification.days,
                        "hour" to notification.hour
                    )
                )
                .addOnSuccessListener { continuation.resume(true) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

    override suspend fun getByUser(userId: String): NotificationEntity =
        suspendCoroutine { continuation ->
            db.collection(NOTIFICATION_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        continuation.resume(
                            NotificationEntity(
                                it.get(DAYS_FIELDS) as List<Int>,
                                it.getString(HOUR_FIELD) ?: STRING_EMPTY
                            )
                        )
                    } else {
                        continuation.resume(NotificationEntity(emptyList(), STRING_EMPTY))
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
}
