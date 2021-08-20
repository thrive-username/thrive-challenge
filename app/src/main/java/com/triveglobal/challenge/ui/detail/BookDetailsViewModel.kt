package com.triveglobal.challenge.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.triveglobal.challenge.di.qualifiers.IODispatcher
import com.triveglobal.challenge.di.qualifiers.MainDispatcher
import com.triveglobal.challenge.extensions.copyAndTransformLastValue
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.model.Constants.Validation.CHECKOUT_NAME_MIN_LENGTH
import com.triveglobal.challenge.repositories.BookRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BookDetailsViewModel @AssistedInject constructor(
    private val bookRepository: BookRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @Assisted("book") private var book: Book
) : ViewModel() {

    private val _uiModelLiveData = MutableLiveData(BookDetailsUIModel(book,))
    val uiModelLiveData : LiveData<BookDetailsUIModel> = _uiModelLiveData

    /**
     * @throws IllegalArgumentException if the [name] parameters is not valid
     */
    fun checkOut(name: String) {
        val sanitizedName = name.trim()
        if (sanitizedName.length < CHECKOUT_NAME_MIN_LENGTH){
            _uiModelLiveData.copyAndTransformLastValue { copy(error = IllegalArgumentException("$sanitizedName is invalid, name must have at least 3 characters"))}
        }else{
            _uiModelLiveData.copyAndTransformLastValue { copy(error = null, loading = true) }
            viewModelScope.launch(mainDispatcher){
                try {
                    val updatedBook = bookRepository.checkOutBook(book, name)
                    book = updatedBook
                    _uiModelLiveData.copyAndTransformLastValue { copy(book = updatedBook, loading = false) }
                }catch (error: Exception) {
                    _uiModelLiveData.copyAndTransformLastValue { copy(error = error, loading = false) }
                }
            }
        }
    }

    fun onErrorDismissed() {
        _uiModelLiveData.copyAndTransformLastValue { copy(error = null) }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("book") book: Book): BookDetailsViewModel
    }


}