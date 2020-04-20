package com.bicifiapp.questions.repository.knowledge.datasource

import com.bicifiapp.questions.repository.knowledge.ArticleKnowledge
import com.squareup.moshi.Json

data class ArticleKnowledgeEntity(

    @Json(name = "articles")
    val articles: List<ArticlesEntity>

) {
    fun toArticleKnowledge(): ArticleKnowledge {
        if (articles.isNotEmpty()) {
            return ArticleKnowledge(
                articles[0].body
            )
        }

        return ArticleKnowledge("")
    }

}

data class ArticlesEntity(

    @Json(name = "id")
    val id: Long,

    @Json(name = "body")
    val body: String

)