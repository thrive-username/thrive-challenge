package com.triveglobal.challenge.com.triveglobal.challenge.repositories

import app.cash.turbine.test
import com.triveglobal.challenge.datasource.local.LocalBookDataSource
import com.triveglobal.challenge.datasource.remote.RemoteBookDataSource
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.model.ResourceState
import com.triveglobal.challenge.repositories.BookRepository
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class BookRepositoryTest {

    @get:Rule
    val errorCollector = ErrorCollector()

    @Mock
    private lateinit var remoteBookDataSource: RemoteBookDataSource

    @Mock
    private lateinit var localBookDataSource: LocalBookDataSource
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var bookRepository: BookRepository
    private val random = Random()

    @Before
    fun setup() {
        bookRepository =
            BookRepository(remoteBookDataSource, localBookDataSource, testCoroutineDispatcher)
    }


    @Test
    fun `BookStream Default with null data, not loading and no error`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        flow.test {
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Refresh, Network Call Error and then 2 items state emitted, no interaction with local data source`() =
        runBlockingTest {
            val flow = bookRepository.booksStream()
            val error = RuntimeException("Some Network Error")
            doThrow(error).`when`(remoteBookDataSource).getAllBooks()
            flow.test {
                bookRepository.refresh()
                errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
                errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
                errorCollector.checkThat(awaitItem(), equalTo(ResourceState(error = error)))
                verifyNoInteractions(localBookDataSource)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `Refresh, Network Call Success and then 2 items state emitted, all books saved on local data source`() =
        runBlockingTest {
            val flow = bookRepository.booksStream()
            val books = listOf(createBook(), createBook(), createBook())
            doReturn(books).`when`(remoteBookDataSource).getAllBooks()
            flow.test {
                bookRepository.refresh()
                errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
                errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
                errorCollector.checkThat(awaitItem(), equalTo(ResourceState(data = books)))
                val argumentCaptor = argumentCaptor<Book>()
                verify(localBookDataSource, times(books.size)).saveOrUpdateBook(argumentCaptor.capture())
                errorCollector.checkThat(argumentCaptor.allValues, equalTo(books))
                cancelAndConsumeRemainingEvents()
            }
        }

    private fun createBook() = Book(
        "author${random.nextInt()}",
        "categories${random.nextInt()}",
        random.nextLong(),
        null,
        null,
        "publisher${random.nextInt()}",
        "title${random.nextInt()}"
    )

}