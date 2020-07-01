package com.bicifiapp.notificationssettings.repository

import co.devhack.base.Either
import co.devhack.base.error.Failure

interface ProfileRepository {

    suspend fun save(profile: Profile): Either<Failure, Boolean>

    suspend fun getByUserId(userId: String): Either<Failure, Profile?>
}
