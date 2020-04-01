package com.bicifiapp.notificationssettings.repository

interface NotificationsRepository {

    suspend fun save(notification: Notification): Boolean

    suspend fun getNotifications(): List<Notification>

}
