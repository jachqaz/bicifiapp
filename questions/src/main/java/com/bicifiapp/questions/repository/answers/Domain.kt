package com.bicifiapp.questions.repository.answers

data class Answer(
    val userId: String,
    val questionId: String,
    val response: String,
    val date: String
)