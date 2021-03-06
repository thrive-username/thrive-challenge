package com.triveglobal.challenge.repositories

import com.triveglobal.challenge.datasource.local.LocalBookDataSource
import com.triveglobal.challenge.datasource.remote.RemoteBookDataSource
import com.triveglobal.challenge.di.qualifiers.IODispatcher
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.model.DateTimeProvider
import com.triveglobal.challenge.model.ResourceState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BookRepository @Inject constructor(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val localBookDataSource: LocalBookDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dateTimeProvider: DateTimeProvider
) {

    //region Private Methods/Properties
    private val scope = CoroutineScope(ioDispatcher)
    private val booksResourceFlow = MutableStateFlow(ResourceState<List<Book>>())

    /**
     * @param throwError flag to indicate whether or  not the error should be thrown or hold inside the flow
     */
    private suspend fun performCrudOperations(
        throwError: Boolean,
        operation: suspend () -> Unit = {}
    ) {
        val currentResourceState = booksResourceFlow.value.copy(loading = true, error = null)
        booksResourceFlow.emit(currentResourceState)
        try {
            operation()
            booksResourceFlow.emit(ResourceState(localBookDataSource.getAllBooks()))
        } catch (error: Exception) {
            if (throwError) {
                booksResourceFlow.emit(currentResourceState.copy(loading = false))
                throw error
            } else {
                booksResourceFlow.emit(currentResourceState.copy(loading = false, error = error))
            }
        }
    }

    //endregion

    //region Public Methods/Properties

    /**
     * @return stream of list of available [Book]
     */
    fun booksStream(): Flow<ResourceState<List<Book>>> {
        return booksResourceFlow
    }

    /**
     * Load the book from the local data store
     */
    fun load() {
        scope.launch {
            performCrudOperations(false)
        }
    }

    /**
     * Pull from the remote data source and updates it locally
     * removing any previous stored book
     */
    fun synchronize() {
        //If we are already refreshing discard this call
        if (booksResourceFlow.value.loading) return
        scope.launch {
            performCrudOperations(false){
                val remoteBooks = remoteBookDataSource.getAllBooks()
                localBookDataSource.deleteAllBooks()
                remoteBooks.forEach { book ->
                    localBookDataSource.saveOrUpdateBook(book)
                }
            }
        }
    }

    /**
     * Adds a new book on the backend, updating the local data source
     */
    suspend fun addBook(book: Book) {
        performCrudOperations(true) {
            val remoteBook = remoteBookDataSource.saveBook(book)
            localBookDataSource.saveOrUpdateBook(remoteBook)
        }
    }

    /**
     * Updates the backend indicating the [book]
     * has been checked out by [checkedOutBy] using the current time
     * on this device
     * @return updated [Book]
     */
    open suspend fun checkOutBook(book: Book, checkedOutBy: String): Book {
        val bookToUpdate = book.copy(
            lastCheckedOutBy = checkedOutBy,
            lastCheckedOut = dateTimeProvider.currentDateTime
        )
        performCrudOperations(true) {
            remoteBookDataSource.updateBook(bookToUpdate)
            localBookDataSource.saveOrUpdateBook(bookToUpdate)
        }
        return bookToUpdate
    }

    /**
     * Removes a book from the  backend and then updates the local data source
     * @throws IllegalArgumentException if the[book] doesn't have an id
     */
    suspend fun removeBook(book: Book) {
        performCrudOperations(true) {
            if (book.id == null) throw IllegalArgumentException("Book $book doesn't have an id")
            remoteBookDataSource.deleteBook(book)
            localBookDataSource.deleteBook(book)
        }
    }

    /**
     * Removes all books from the backend and then update the local data source
     */
    suspend fun removeAllBooks() {
        performCrudOperations(true) {
            remoteBookDataSource.deleteAllBooks()
            localBookDataSource.deleteAllBooks()
        }
    }


    //endregion


}