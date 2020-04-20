package com.bicifiapp.questions.repository.knowledge.datasource

import com.bicifiapp.questions.repository.knowledge.network.ArticlesApi

class DataSourceKnowledgeNetwork(
    private val articlesApi: ArticlesApi
) : DataSourceKnowledge {

    override suspend fun getArticleByLabel(label: String): ArticleKnowledgeEntity =
        articlesApi.getArticleByLabel(label)

}
