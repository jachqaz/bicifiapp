package com.bicifiapp

import android.app.Application
import com.bicifiapp.di.answersRepositoryModule
import com.bicifiapp.di.emotionalQuestionViewModelModule
import com.bicifiapp.di.homeViewModelModule
import com.bicifiapp.di.knowledgeRepositoryModule
import com.bicifiapp.di.networkHandlerModule
import com.bicifiapp.di.notificationModule
import com.bicifiapp.di.profileRepositoryModule
import com.bicifiapp.di.profileViewModelModule
import com.bicifiapp.di.questionRepositoryModule
import com.bicifiapp.di.questionViewModelModule
import com.bicifiapp.di.statisticsRepositoryModule
import com.bicifiapp.di.statisticsViewModelModule
import com.stripe.android.PaymentConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ApplicationBificiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initStripe()
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
                    knowledgeRepositoryModule,
                    statisticsRepositoryModule,
                    homeViewModelModule,
                    statisticsViewModelModule,
                    emotionalQuestionViewModelModule,
                    notificationModule
                )
            )
        }
    }

    private fun initStripe() {
        PaymentConfiguration.init(
            applicationContext,
            BuildConfig.public_key_stripe
        )
    }
}
