package com.bicifiapp.notificationssettings.notification

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.notificationssettings.notification.datasources.NotificationDataSource
import com.bicifiapp.notificationssettings.notification.datasources.NotificationEntity

class NotificationRepositoryImp(
    private val firestoreDataSource: NotificationDataSource,
    private val networkHandler: NetworkHandler
) : NotificationRepository {

    override suspend fun save(
        userId: String,
        notification: Notification
    ): Either<Failure, Boolean> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    firestoreDataSource.save(
                        NotificationEntity(
                            notification.days,
                            notification.hour
                        ),
                        userId
                    )
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }

    override suspend fun getByUser(userId: String): Either<Failure, Notification> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    firestoreDataSource.getByUser(userId).toNotification()
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }
}
