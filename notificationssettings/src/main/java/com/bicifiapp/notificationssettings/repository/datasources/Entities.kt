package com.bicifiapp.notificationssettings.repository.datasources

import com.bicifiapp.notificationssettings.repository.Perfil

data class PerfilEntity(
    val id: String,
    val birthDay: String,
    val familyRol: String,
    val educationLevel: String,
    val yearExperience: Int,
    val acceptNotification: Boolean,
    val acceptLocation: Boolean
) {
    fun toPerfil() =
        Perfil(
            id,
            birthDay,
            familyRol,
            educationLevel,
            yearExperience,
            acceptNotification,
            acceptLocation
        )
}