package com.bicifiapp.notificationssettings.repository.datasources

interface DataSourceNotificationSettings {

    suspend fun save(profileEntity: ProfileEntity): Boolean

    suspend fun getByUserId(userId: String): ProfileEntity?
}
