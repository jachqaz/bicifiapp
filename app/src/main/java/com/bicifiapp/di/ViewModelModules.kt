package com.bicifiapp.di

import com.bicifiapp.ui.viewmodels.emotionalquestion.EmotionalQuestionViewModel
import com.bicifiapp.ui.viewmodels.home.HomeViewModel
import com.bicifiapp.ui.viewmodels.profile.ProfileViewModel
import com.bicifiapp.ui.viewmodels.questions.ArticleViewModel
import com.bicifiapp.ui.viewmodels.questions.QuestionViewModel
import com.bicifiapp.ui.viewmodels.statistics.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileViewModelModule = module {

    viewModel {
        ProfileViewModel(get())
    }
}

val questionViewModelModule = module {

    viewModel {
        QuestionViewModel(get(), get())
    }

    viewModel {
        ArticleViewModel(get())
    }

}

val homeViewModelModule = module {

    viewModel {
        HomeViewModel(get())
    }
}

val statisticsViewModelModule = module {

    viewModel {
        StatisticsViewModel(get())
    }
}

val emotionalQuestionViewModelModule = module {

    viewModel {
        EmotionalQuestionViewModel(get())
    }
}
