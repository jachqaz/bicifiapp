package com.bicifiapp.notificationssettings.repository.datasources

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DataSourceFirestore : DataSourceNotificationSettings {

    private companion object {
        const val USERS_COLLECTION = "users"
    }

    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun save(profileEntity: ProfileEntity): Boolean =
        suspendCoroutine { continuation ->
            db.collection(USERS_COLLECTION).document(profileEntity.userId)
                .set(
                    profileEntity
                ).addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    override suspend fun getByUserId(userId: String): ProfileEntity? =
        suspendCoroutine { continuation ->
            db.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener {
                    continuation.resume(it.toObject(ProfileEntity::class.java))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
}
