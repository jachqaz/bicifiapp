package com.bicifiapp

import android.app.Application
import com.bicifiapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ApplicationBificiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@ApplicationBificiApp)
            modules(
                listOf(
                    networkHandlerModule,
                    profileRepositoryModule,
                    profileViewModelModule,
                    questionRepositoryModule,
                    answersRepositoryModule,
                    questionViewModelModule,
                    knowledgeRepositoryModule
                )
            )
        }
    }
}