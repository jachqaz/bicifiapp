package com.bicifiapp.questions.repository.question

data class Question(
    val id: String,
    val order: Int,
    val text: String,
    val label: String
)