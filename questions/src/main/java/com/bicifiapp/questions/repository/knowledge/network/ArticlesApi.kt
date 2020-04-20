package com.bicifiapp.questions.repository.knowledge.network

import com.bicifiapp.questions.repository.knowledge.datasource.ArticleKnowledgeEntity
import com.e.questions.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApi {

    companion object {
        const val PATH_LABEL = "label_names"
    }

    @GET(BuildConfig.END_POINT_GET_ARTICLE_BY_LABEL)
    suspend fun getArticleByLabel(@Query(PATH_LABEL) label: String): ArticleKnowledgeEntity
}
