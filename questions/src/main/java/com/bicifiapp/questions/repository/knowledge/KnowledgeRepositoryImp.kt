package com.bicifiapp.questions.repository.knowledge

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.bicifiapp.questions.repository.knowledge.datasource.DataSourceKnowledge

class KnowledgeRepositoryImp(
    private val dataSourceKnowledge: DataSourceKnowledge,
    private val networkHandler: NetworkHandler
) : KnowledgeRepository {

    override suspend fun getArticleByLabel(label: String): Either<Failure, ArticleKnowledge> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSourceKnowledge.getArticleByLabel(label).toArticleKnowledge()
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (e: Exception) {
            Either.Left(e.toCustomExceptions())
        }
}
