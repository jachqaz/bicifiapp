package com.bicifiapp.notificationssettings.notification.datasources

import com.bicifiapp.notificationssettings.notification.Notification

data class NotificationEntity(val days: List<Int>, val hour: String) {
    fun toNotification() =
        Notification(
            days,
            hour
        )
}
