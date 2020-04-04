package com.bicifiapp.notificationssettings.repository

data class Perfil(
    val id: String,
    val birthDay: String,
    val familyRol: String,
    val educationLevel: String,
    val yearExperience: Int,
    val acceptNotification: Boolean,
    val acceptLocation: Boolean
)