package com.triveglobal.challenge.ui.feed

import com.triveglobal.challenge.model.Book

data class BookFeedUIModel(val items: List<Book>? = null, val loading: Boolean = false, val error: Exception? = null)