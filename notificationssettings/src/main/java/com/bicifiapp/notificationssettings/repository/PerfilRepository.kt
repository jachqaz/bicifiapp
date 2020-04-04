package com.bicifiapp.notificationssettings.repository

interface PerfilRepository {

    suspend fun save(perfil: Perfil): Boolean

    suspend fun getPerfils(): List<Perfil>

}
