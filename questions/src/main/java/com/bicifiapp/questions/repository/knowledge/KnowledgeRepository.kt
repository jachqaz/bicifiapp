package com.bicifiapp.questions.repository.knowledge

import co.devhack.base.Either
import co.devhack.base.error.Failure

interface KnowledgeRepository {

    suspend fun getArticleByLabel(label: String): Either<Failure, ArticleKnowledge>

}
