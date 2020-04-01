package com.bicifiapp.notificationssettings.repository

import com.bicifiapp.notificationssettings.repository.datasources.DataSource
import com.bicifiapp.notificationssettings.repository.datasources.NotificationEntity

class NotificationsRepositoryImp(
    private val dataSource: DataSource
) : NotificationsRepository {

    override suspend fun save(notification: Notification): Boolean =
        dataSource.save(NotificationEntity(notification.id, notification.accept))

    override suspend fun getNotifications(): List<Notification> =
        dataSource.getNotifications().map {
            it.toNotification()
        }

}