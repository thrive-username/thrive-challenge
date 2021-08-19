package com.triveglobal.challenge.ui.detail

import androidx.lifecycle.ViewModel
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.repositories.BookRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class BookDetailsViewModel @AssistedInject constructor(
    private val bookRepository: BookRepository,
    @Assisted("book") private val book: Book
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("book") book: Book): BookDetailsViewModel
    }

}