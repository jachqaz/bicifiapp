package com.bicifiapp.di

import co.devhack.base.network.Network.createNetworkClient
import com.bicifiapp.BuildConfig
import com.bicifiapp.notificationssettings.repository.ProfileRepository
import com.bicifiapp.notificationssettings.repository.ProfileRepositoryImp
import com.bicifiapp.notificationssettings.repository.datasources.DataSourceFirestore
import com.bicifiapp.notificationssettings.repository.datasources.DataSourceNotificationSettings
import com.bicifiapp.questions.repository.answers.AnswersRepository
import com.bicifiapp.questions.repository.answers.AnswersRepositoryImp
import com.bicifiapp.questions.repository.answers.datasource.DataSourceAnswer
import com.bicifiapp.questions.repository.answers.datasource.DataSourceAnswerFirebase
import com.bicifiapp.questions.repository.knowledge.KnowledgeRepository
import com.bicifiapp.questions.repository.knowledge.KnowledgeRepositoryImp
import com.bicifiapp.questions.repository.knowledge.datasource.DataSourceKnowledge
import com.bicifiapp.questions.repository.knowledge.datasource.DataSourceKnowledgeNetwork
import com.bicifiapp.questions.repository.knowledge.network.ArticlesApi
import com.bicifiapp.questions.repository.question.QuestionRepository
import com.bicifiapp.questions.repository.question.QuestionRepositoryImp
import com.bicifiapp.questions.repository.question.datasources.DataSourceQuestion
import com.bicifiapp.questions.repository.question.datasources.DataSourceQuestionFirebase
import com.bicifiapp.statistics.StatisticsRepository
import com.bicifiapp.statistics.StatisticsRepositoryImp
import com.bicifiapp.statistics.datasource.StatisticsDataSource
import com.bicifiapp.statistics.datasource.StatisticsDataSourceFirebase
import org.koin.dsl.module

val profileRepositoryModule = module {

    factory<ProfileRepository> {
        ProfileRepositoryImp(get(), get())
    }

    factory<DataSourceNotificationSettings> {
        DataSourceFirestore()
    }

}

val questionRepositoryModule = module {

    factory<QuestionRepository> {
        QuestionRepositoryImp(get(), get())
    }

    factory<DataSourceQuestion> {
        DataSourceQuestionFirebase()
    }

}

val answersRepositoryModule = module {

    factory<AnswersRepository> {
        AnswersRepositoryImp(get(), get())
    }

    factory<DataSourceAnswer> {
        DataSourceAnswerFirebase()
    }

}

val knowledgeRepositoryModule = module {

    factory<KnowledgeRepository> {
        KnowledgeRepositoryImp(get(), get())
    }

    factory<DataSourceKnowledge> {
        DataSourceKnowledgeNetwork(
            get()
        )
    }

    single<ArticlesApi> {
        createNetworkClient(
            baseUrl = BuildConfig.BASE_URL_ZENDESK,
            debug = BuildConfig.DEBUG
        )
            .create(ArticlesApi::class.java)
    }
}

val statisticsRepositoryModule = module {

    factory<StatisticsRepository> {
        StatisticsRepositoryImp(get())
    }

    factory<StatisticsDataSource> {
        StatisticsDataSourceFirebase()
    }
}
