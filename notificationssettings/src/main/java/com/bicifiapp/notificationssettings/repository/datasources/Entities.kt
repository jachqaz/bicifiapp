package com.bicifiapp.notificationssettings.repository.datasources

import com.bicifiapp.notificationssettings.repository.Notification

data class NotificationEntity(
    val id: String,
    val accept: Boolean
) {
    fun toNotification() =
        Notification(
            id,
            accept
        )
}