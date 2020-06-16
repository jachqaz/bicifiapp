package com.bicifiapp.notificationssettings.notification.datasources

interface NotificationDataSource {

    suspend fun save(notification: NotificationEntity, userId: String): Boolean

    suspend fun getByUser(userId: String): NotificationEntity
}
