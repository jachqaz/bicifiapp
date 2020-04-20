package com.bicifiapp.ui.viewmodels.questions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.knowledge.ArticleKnowledge
import com.bicifiapp.questions.repository.knowledge.KnowledgeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleViewModel(
    private val knowledgeRepository: KnowledgeRepository
) : ViewModel() {

    private lateinit var resultArticle: Either<Failure, ArticleKnowledge>

    val resultArticleKnowledgeLiveData by lazy {
        MutableLiveData<State>()
    }

    fun getArticleByLabel(label: String) {
        resultArticleKnowledgeLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultArticle = knowledgeRepository.getArticleByLabel(label)
            }
            resultArticle.either(::handleFailureArticleByLabel, ::getArticleByLabelSuccess)
        }
    }

    private fun getArticleByLabelSuccess(articleKnowledge: ArticleKnowledge) {
        resultArticleKnowledgeLiveData.value = State.Success(articleKnowledge.text)
    }

    private fun handleFailureArticleByLabel(failure: Failure) {
        resultArticleKnowledgeLiveData.value = State.Failed(failure)
    }
}