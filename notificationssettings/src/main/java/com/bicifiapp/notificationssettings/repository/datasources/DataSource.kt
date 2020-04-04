package com.bicifiapp.notificationssettings.repository.datasources

interface DataSource {

    suspend fun save(perfilEntity: PerfilEntity): Boolean

    suspend fun getPerfils(): List<PerfilEntity>

}