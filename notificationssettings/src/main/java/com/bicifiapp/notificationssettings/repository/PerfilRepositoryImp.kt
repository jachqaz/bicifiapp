package com.bicifiapp.notificationssettings.repository

import com.bicifiapp.notificationssettings.repository.datasources.DataSource
import com.bicifiapp.notificationssettings.repository.datasources.PerfilEntity

class PerfilRepositoryImp(
    private val dataSource: DataSource
) : PerfilRepository {

    override suspend fun save(perfil: Perfil): Boolean =
        dataSource.save(PerfilEntity(perfil.id, perfil.birthDay, perfil.familyRol, perfil.educationLevel, perfil.yearExperience, perfil.acceptNotification, perfil.acceptLocation))

    override suspend fun getPerfils(): List<Perfil> =
        dataSource.getPerfils().map {
            it.toPerfil()
        }

}