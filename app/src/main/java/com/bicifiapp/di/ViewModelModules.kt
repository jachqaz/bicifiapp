package com.bicifiapp.di

import com.bicifiapp.ui.viewmodels.profile.ProfileViewModel
import com.bicifiapp.ui.viewmodels.questions.ArticleViewModel
import com.bicifiapp.ui.viewmodels.questions.QuestionViewModel
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
