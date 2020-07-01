package com.bicifiapp.notificationssettings.repository.datasources

import com.bicifiapp.notificationssettings.repository.Profile
import com.google.firebase.firestore.Exclude

data class ProfileEntity(
    @Exclude
    val userId: String = "",
    val birthDate: String = "",
    val familyRole: String = "",
    val educationLevel: String = "",
    val yearExperience: Int = 0,
    val acceptNotification: Boolean = false,
    val acceptLocation: Boolean = false
) {
    fun toProfile() =
        Profile(
            userId,
            birthDate,
            familyRole,
            educationLevel,
            yearExperience,
            acceptNotification,
            acceptLocation
        )
}
