package com.bicifiapp.notificationssettings.repository

data class Profile(
    val userId: String,
    val birthDate: String,
    val familyRole: String,
    val educationLevel: String,
    val yearExperience: Int,
    val acceptNotification: Boolean,
    val acceptLocation: Boolean
)
