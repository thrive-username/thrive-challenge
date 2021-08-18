package com.triveglobal.challenge.ui.feed

import com.triveglobal.challenge.model.Book

data class BookFeedUIModel(val items: List<Book>, val loading: Boolean, val error: Exception?)