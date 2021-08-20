package com.triveglobal.challenge.ui.detail

import com.triveglobal.challenge.model.Book

data class BookDetailsUIModel(
    val book: Book,
    val error: Exception? = null,
    val loading: Boolean = false,
    val displaySuccessMessage: Boolean = false
)