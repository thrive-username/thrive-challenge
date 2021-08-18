package com.triveglobal.challenge.ui.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.triveglobal.challenge.repositories.BookRepository
import com.triveglobal.challenge.ui.utils.SavedStateViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class BookFeedViewModel @AssistedInject constructor(
    private val bookRepository: BookRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        //TODO: Register to the book flow
    }

    override fun onCleared() {
        super.onCleared()
        //TODO: Unregister to the book flow
    }

    @AssistedFactory
    interface Factory : SavedStateViewModelFactory<BookFeedViewModel>

}