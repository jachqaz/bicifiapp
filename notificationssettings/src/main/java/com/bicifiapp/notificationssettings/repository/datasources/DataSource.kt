package com.bicifiapp.notificationssettings.repository.datasources

interface DataSource {

    suspend fun save(notificationEntity: NotificationEntity): Boolean

    suspend fun getNotifications(): List<NotificationEntity>

}