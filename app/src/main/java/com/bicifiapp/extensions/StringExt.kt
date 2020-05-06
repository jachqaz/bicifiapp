package com.bicifiapp.extensions

fun String?.safeString() = this ?: ""

fun String.Companion.empty() = ""