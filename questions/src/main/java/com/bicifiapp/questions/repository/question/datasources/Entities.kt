package com.bicifiapp.questions.repository.question.datasources

import com.bicifiapp.questions.repository.question.Question

data class QuestionEntity(
    val id: String = "",
    val order: Int = 0,
    val text: String = "",
    val label: String = ""
) {
    fun toQuestion() =
        Question(
            id,
            order,
            text,
            label
        )
}