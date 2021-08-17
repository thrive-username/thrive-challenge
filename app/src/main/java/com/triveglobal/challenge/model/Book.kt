package com.triveglobal.challenge.model

import org.joda.time.DateTime

data class Book(
    val author: String,
    val categories: String,
    val id: Long,
    val lastCheckedOut: DateTime?,
    val lastCheckedOutBy: String?,
    val publisher: String,
    val title: String
)
