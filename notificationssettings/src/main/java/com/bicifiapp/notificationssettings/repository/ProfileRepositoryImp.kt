package com.bicifiapp.notificationssettings.repository

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.notificationssettings.repository.datasources.DataSourceNotificationSettings
import com.bicifiapp.notificationssettings.repository.datasources.ProfileEntity

class ProfileRepositoryImp(
    private val dataSourceNotificationSettings: DataSourceNotificationSettings,
    private val networkHandler: NetworkHandler
) : ProfileRepository {

    override suspend fun save(profile: Profile): Either<Failure, Boolean> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSourceNotificationSettings.save(
                        ProfileEntity(
                            profile.userId,
                            profile.birthDate,
                            profile.familyRole,
                            profile.educationLevel,
                            profile.yearExperience,
                            profile.acceptNotification,
                            profile.acceptLocation
                        )
                    )
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (exception: Exception) {
            Either.Left(exception.toCustomExceptions())
        }

    override suspend fun getByUserId(userId: String): Either<Failure, Profile?> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSourceNotificationSettings.getByUserId(userId)?.toProfile()
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (exception: Exception) {
            Either.Left(exception.toCustomExceptions())
        }
}
