package com.triveglobal.challenge.repositories

import app.cash.turbine.test
import com.triveglobal.challenge.BookMockHelper.createBook
import com.triveglobal.challenge.datasource.local.LocalBookDataSource
import com.triveglobal.challenge.datasource.remote.RemoteBookDataSource
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.model.DateTimeProvider
import com.triveglobal.challenge.model.ResourceState
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor

@RunWith(MockitoJUnitRunner::class)
class BookRepositoryTest {

    @get:Rule
    val errorCollector = ErrorCollector()
    @Mock
    private lateinit var remoteBookDataSource: RemoteBookDataSource
    @Mock
    private lateinit var localBookDataSource: LocalBookDataSource
    @Mock
    private lateinit var dateTimeProvider: DateTimeProvider
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var bookRepository: BookRepository

    @Before
    fun setup() {
        bookRepository =
            BookRepository(remoteBookDataSource, localBookDataSource, testCoroutineDispatcher, dateTimeProvider)
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
    fun `refresh, Network Call Error and then 2 items state emitted, no interaction with local data source`() =
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
    fun `refresh, Network Call Success and then 2 items state emitted, all books saved on local data source`() =
        runBlockingTest {
            val flow = bookRepository.booksStream()
            val books = listOf(createBook(), createBook(), createBook())
            doReturn(books).`when`(remoteBookDataSource).getAllBooks()
            doReturn(books).`when`(localBookDataSource).getAllBooks()
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

    @Test(expected = RuntimeException::class)
    fun `addBook, Network Call Error and then 2 items state emitted, no interaction with local data source`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val book = createBook()
        val error = RuntimeException("Some Network Error")
        doThrow(error).`when`(remoteBookDataSource).saveBook(book)
        flow.test {
            bookRepository.addBook(book)
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(error = error)))
            verifyNoInteractions(localBookDataSource)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `addBook, Network Call Success and then 2 items state emitted, remote book store on local data source`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val remoteBook = createBook()
        val book = createBook()
        doReturn(remoteBook).`when`(remoteBookDataSource).saveBook(book)
        val booksStored = listOf(createBook(), createBook(), remoteBook)
        doReturn(booksStored).`when`(localBookDataSource).getAllBooks()
        flow.test {
            bookRepository.addBook(book)
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(data = booksStored)))
            verify(localBookDataSource).saveOrUpdateBook(remoteBook)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `checkOutBook, Network call updating the checkout name and date, Success, update local data source, book update returned`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val book = createBook()
        val currentDateTime = DateTime.now().minusDays(1)
        doReturn(currentDateTime).`when`(dateTimeProvider).currentDateTime
        val checkedOutBy = "John Doe"
        val booksStored = listOf(createBook(), createBook(), checkedOutBy)
        doReturn(booksStored).`when`(localBookDataSource).getAllBooks()
        flow.test {
            val updatedBook = bookRepository.checkOutBook(book, checkedOutBy)
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(data = booksStored)))
            errorCollector.checkThat(updatedBook, equalTo(book.copy(lastCheckedOut = currentDateTime, lastCheckedOutBy = checkedOutBy)))
            verify(remoteBookDataSource).updateBook(updatedBook)
            verify(localBookDataSource).saveOrUpdateBook(updatedBook)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test(expected = RuntimeException::class)
    fun `checkOutBook, Network call updating the checkout name and date, Error, local data source not invoked`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val book = createBook()
        val error = RuntimeException("Some Network Error")
        doThrow(error).`when`(remoteBookDataSource).updateBook(org.mockito.kotlin.any())
        val currentDateTime = DateTime.now().minusDays(1)
        doReturn(currentDateTime).`when`(dateTimeProvider).currentDateTime
        val checkedOutBy = "John Doe"
        flow.test {
            bookRepository.checkOutBook(book, checkedOutBy)
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(error = error)))
            val updatedBook = book.copy(lastCheckedOut = currentDateTime, lastCheckedOutBy = checkedOutBy)
            verify(remoteBookDataSource).updateBook(updatedBook)
            verify(localBookDataSource, never()).saveOrUpdateBook(updatedBook)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `removeBook, with Id, Network call success, 2 states emitted, local data source updated`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val book = createBook()
        val booksStored = listOf(createBook(), createBook())
        doReturn(booksStored).`when`(localBookDataSource).getAllBooks()
        flow.test {
            bookRepository.removeBook(book)
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(data = booksStored)))
            verify(remoteBookDataSource).deleteBook(book)
            verify(localBookDataSource).deleteBook(book)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `removeBook, without Id, throw error, no new state emitted, no interaction with local or remote data sources`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val book = createBook(id = null)
        flow.test {
            bookRepository.removeBook(book)
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            verifyNoInteractions(remoteBookDataSource)
            verifyNoInteractions(localBookDataSource)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test(expected = RuntimeException::class)
    fun `removeBook, with Id, network error,2 new states emitted, throw error, no interaction with local data sources`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val book = createBook()
        val error = RuntimeException("Some Network Error")
        doThrow(error).`when`(remoteBookDataSource).deleteBook(book)
        flow.test {
            bookRepository.removeBook(book)
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(error = error)))
            verify(localBookDataSource, never()).deleteBook(book)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `removeAllBooks, network success, 2 new states emitted, update local data source`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val booksStored = listOf<Book>()
        doReturn(booksStored).`when`(localBookDataSource).getAllBooks()
        flow.test {
            bookRepository.removeAllBooks()
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(data = booksStored)))
            verify(remoteBookDataSource).deleteAllBooks()
            verify(localBookDataSource).deleteAllBooks()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test(expected = RuntimeException::class)
    fun `removeAllBooks, network error, throw error, 2 new states emitted, no interaction with local data source`() = runBlockingTest {
        val flow = bookRepository.booksStream()
        val error = RuntimeException("Some Network Error")
        doThrow(error).`when`(remoteBookDataSource).deleteAllBooks()
        flow.test {
            bookRepository.removeAllBooks()
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState()))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(loading = true)))
            errorCollector.checkThat(awaitItem(), equalTo(ResourceState(error = error)))
            verifyNoInteractions(localBookDataSource)
            cancelAndConsumeRemainingEvents()
        }
    }

}