package com.bicifiapp.questions.repository.answers

data class Answer(
    val userId: String,
    val questionId: String,
    val response: String,
    val date: String
)

data class LastUserLevelRecord(
    val lastLevel: Int,
    val dateLastLevel: String,
    val titleLevel: String,
    val descriptionLevel: String
)