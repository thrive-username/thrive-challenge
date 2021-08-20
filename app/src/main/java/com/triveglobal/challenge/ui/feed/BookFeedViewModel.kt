package com.triveglobal.challenge.ui.feed

import androidx.lifecycle.*
import com.triveglobal.challenge.di.qualifiers.IODispatcher
import com.triveglobal.challenge.extensions.copyAndTransformLastValue
import com.triveglobal.challenge.repositories.BookRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookFeedViewModel @AssistedInject constructor(
    private val bookRepository: BookRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiModelLiveData = MutableLiveData<BookFeedUIModel>()
    val uiModelLiveData : LiveData<BookFeedUIModel> = _uiModelLiveData
    private val booksFlowJob: Job = viewModelScope.launch(ioDispatcher){
        bookRepository.booksStream().collect { resourceState ->
            _uiModelLiveData.value = resourceState.run { BookFeedUIModel(data, loading, error) }
        }
    }

    init {
        bookRepository.load()
    }

    fun synchronize() {
        bookRepository.synchronize()
    }

    fun onErrorDismissed() {
        _uiModelLiveData.copyAndTransformLastValue { copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        booksFlowJob.cancel()
    }

    @AssistedFactory
    interface Factory {
        fun create(): BookFeedViewModel
    }

}