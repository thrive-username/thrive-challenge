package com.triveglobal.challenge.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.triveglobal.challenge.BookMockHelper
import com.triveglobal.challenge.LiveDataTestObserver
import com.triveglobal.challenge.LiveDataTestObserver.Companion.createTestObserver
import com.triveglobal.challenge.repositories.BookRepository
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow

@RunWith(MockitoJUnitRunner::class)
class BookDetailsViewModelTest {

    @get:Rule
    val errorCollector = ErrorCollector()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var bookRepository: BookRepository
    private val book = BookMockHelper.createBook()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var bookDetailsViewModel: BookDetailsViewModel
    private lateinit var liveDataTestObserver: LiveDataTestObserver<BookDetailsUIModel>

    @Before
    fun setup() {
        bookDetailsViewModel = BookDetailsViewModel(bookRepository, testCoroutineDispatcher, book)
        liveDataTestObserver = bookDetailsViewModel.uiModelLiveData.createTestObserver()
    }

    @Test
    fun `default UI model state, no loading, error, success message, and book`() {
        errorCollector.checkThat(1, equalTo(liveDataTestObserver.emittedValues.size))
        liveDataTestObserver.emittedValues.firstOrNull()?.apply {
            errorCollector.checkThat(book, equalTo(book))
            errorCollector.checkThat(error, nullValue())
            errorCollector.checkThat(loading, equalTo(false))
        }
    }

    @Test
    fun `checkOut and name less than 3 characters, state with IllegalArgumentException emitted`() {
        val name = "ab"
        bookDetailsViewModel.checkOut(name)
        errorCollector.checkThat(2, equalTo(liveDataTestObserver.emittedValues.size))
        liveDataTestObserver.emittedValues[1]?.apply {
            errorCollector.checkThat(book, equalTo(book))
            errorCollector.checkThat(error, instanceOf(IllegalArgumentException::class.java))
            errorCollector.checkThat(loading, equalTo(false))
        }
    }

    @Test
    fun `checkOut with valid name, no error on repository, 2 states emitted, loading and success`() = testCoroutineDispatcher.runBlockingTest {
        val name = "John"
        val updateBook = BookMockHelper.createBook()
        doReturn(updateBook).`when`(bookRepository).checkOutBook(book, name)
        bookDetailsViewModel.checkOut(name)
        errorCollector.checkThat(3, equalTo(liveDataTestObserver.emittedValues.size))
        liveDataTestObserver.emittedValues[1]?.apply {
            errorCollector.checkThat(book, equalTo(book))
            errorCollector.checkThat(error, nullValue())
            errorCollector.checkThat(loading, equalTo(true))
        }
        liveDataTestObserver.emittedValues[2]?.apply {
            errorCollector.checkThat(book, equalTo(updateBook))
            errorCollector.checkThat(error, nullValue())
            errorCollector.checkThat(loading, equalTo(false))
        }
    }

    @Test
    fun `checkOut with valid name, error on repository, 2 states emitted, loading no success and error`() = testCoroutineDispatcher.runBlockingTest {
        val name = "John"
        val repositoryError = RuntimeException("Some Error")
        doThrow(repositoryError).`when`(bookRepository).checkOutBook(book, name)
        bookDetailsViewModel.checkOut(name)
        errorCollector.checkThat(3, equalTo(liveDataTestObserver.emittedValues.size))
        liveDataTestObserver.emittedValues[1]?.apply {
            errorCollector.checkThat(book, equalTo(book))
            errorCollector.checkThat(error, nullValue())
            errorCollector.checkThat(loading, equalTo(true))
        }
        liveDataTestObserver.emittedValues[2]?.apply {
            errorCollector.checkThat(book, equalTo(book))
            errorCollector.checkThat(error, equalTo(repositoryError))
            errorCollector.checkThat(loading, equalTo(false))
        }
    }

    @Test
    fun `error state, invoke onErrorDismissed, state without error is emitted`() {
        `checkOut and name less than 3 characters, state with IllegalArgumentException emitted`()
        bookDetailsViewModel.onErrorDismissed()
        errorCollector.checkThat(3, equalTo(liveDataTestObserver.emittedValues.size))
        liveDataTestObserver.emittedValues[2]?.apply {
            errorCollector.checkThat(book, equalTo(book))
            errorCollector.checkThat(error, nullValue())
            errorCollector.checkThat(loading, equalTo(false))
        }
    }

}
