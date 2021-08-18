package com.triveglobal.challenge.repositories

import com.triveglobal.challenge.datasource.local.LocalBookDataSource
import com.triveglobal.challenge.datasource.remote.RemoteBookDataSource
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.model.ResourceState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BookRepository @Inject constructor(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val localBookDataSource: LocalBookDataSource,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    //region Private Methods/Properties
    private val scope = CoroutineScope(coroutineDispatcher)
    private val booksResourceFlow = MutableStateFlow(ResourceState<List<Book>>())
    //endregion

    //region Public Methods/Properties

    /**
     * @return stream of list of available [Book]
     */
    fun booksStream (): Flow<ResourceState<List<Book>>> {
        return booksResourceFlow
    }

    /**
     * Pull from the remote data source and updates it locally
     */
    fun refresh() {
        //If we are already refreshing discard this call
        if (booksResourceFlow.value.loading) return
        scope.launch{
            val currentResourceState = booksResourceFlow.value.copy(loading = true, error = null)
            booksResourceFlow.emit(currentResourceState)
            try {
                val remoteBooks = remoteBookDataSource.getAllBooks()
                remoteBooks.forEach { book ->
                    localBookDataSource.saveOrUpdateBook(book)
                }
                booksResourceFlow.emit(ResourceState(remoteBooks, null, false))
            }catch (error: Exception) {
                booksResourceFlow.emit(currentResourceState.copy(loading = false, error = error))
            }
        }
    }
    //endregion


}