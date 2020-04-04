package com.bicifiapp.notificationssettings.repository.datasources

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DataSourceFirestore : DataSource {

    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun save(perfilEntity: PerfilEntity): Boolean =
        suspendCoroutine { continuation ->
            db.collection("users").document(perfilEntity.id)
                .set(
                    perfilEntity
                ).addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    override suspend fun getPerfils(): List<PerfilEntity> =
        suspendCoroutine { continuation ->
            db.collection("users").get(
            ).addOnSuccessListener {
                continuation.resume(it.toObjects(PerfilEntity::class.java))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
}