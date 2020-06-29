package com.bicifiapp.notificationssettings.notification

import co.devhack.base.Either
import co.devhack.base.error.Failure

interface NotificationRepository {

    suspend fun save(userId: String, notification: Notification): Either<Failure, Boolean>

    suspend fun getByUser(userId: String): Either<Failure, Notification>
}
