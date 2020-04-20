package com.bicifiapp.questions.repository.knowledge.datasource

interface DataSourceKnowledge {

    suspend fun getArticleByLabel(label: String): ArticleKnowledgeEntity

}
