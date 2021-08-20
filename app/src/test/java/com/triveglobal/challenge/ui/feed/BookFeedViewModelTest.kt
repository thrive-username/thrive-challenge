package com.triveglobal.challenge.ui.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.triveglobal.challenge.BookMockHelper.createBook
import com.triveglobal.challenge.LiveDataTestObserver
import com.triveglobal.challenge.LiveDataTestObserver.Companion.createTestObserver
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.model.ResourceState
import com.triveglobal.challenge.repositories.BookRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class BookFeedViewModelTest {

    @get:Rule
    val errorCollector = ErrorCollector()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var bookRepository: BookRepository
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var bookFeedViewModel: BookFeedViewModel
    private lateinit var liveDataTestObserver: LiveDataTestObserver<BookFeedUIModel>

    @Before
    fun setup() {
        doReturn(flowOf<ResourceState<List<Book>>>()).`when`(bookRepository).booksStream() //Empty flow by default
        initViewModelAndTestObserver()
    }

    @Test
    fun `as soon as the view model is created it should try to invoke load from repository`() {
        verify(bookRepository).load()
    }

    @Test
    fun `synchronize, invoke synchronize from repository`() {
        bookFeedViewModel.synchronize()
        verify(bookRepository).synchronize()
    }

    @Test
    fun `check live data is emitted based on the resource state given by the flow, with items`() = testCoroutineDispatcher.runBlockingTest {
        val resourceState1 = ResourceState<List<Book>>(loading = true)
        val resourceState2 = ResourceState(listOf(createBook(), createBook(), createBook()))
        val resourceState3 = ResourceState<List<Book>>(error = RuntimeException("Some Error"))
        val mockFlow = MutableSharedFlow<ResourceState<List<Book>>>()
        doReturn(mockFlow).`when`(bookRepository).booksStream()
        initViewModelAndTestObserver()
        mockFlow.emit(resourceState1)
        mockFlow.emit(resourceState2)
        mockFlow.emit(resourceState3)
        errorCollector.checkThat(liveDataTestObserver.emittedValues.size, equalTo(3))
        evaluateResourceStateVsBookFeedUIModel(resourceState1, liveDataTestObserver.emittedValues[0])
        evaluateResourceStateVsBookFeedUIModel(resourceState2, liveDataTestObserver.emittedValues[1])
        evaluateResourceStateVsBookFeedUIModel(resourceState3, liveDataTestObserver.emittedValues[2])
    }

    @Test
    fun `while on error state, invoke onErrorDismissed, new ui model should be emitted without error`() = testCoroutineDispatcher.runBlockingTest {
        val resourceState = ResourceState<List<Book>>(error = RuntimeException("Some Error"))
        doReturn(flowOf(resourceState)).`when`(bookRepository).booksStream()
        initViewModelAndTestObserver()
        bookFeedViewModel.onErrorDismissed()
        errorCollector.checkThat(liveDataTestObserver.emittedValues.size, equalTo(2))
        evaluateResourceStateVsBookFeedUIModel(resourceState, liveDataTestObserver.emittedValues[0])
        errorCollector.checkThat(BookFeedUIModel(null, false, null), equalTo(liveDataTestObserver.emittedValues[1]))
    }

    private fun evaluateResourceStateVsBookFeedUIModel(resourceState: ResourceState<List<Book>>, bookFeedUIModel: BookFeedUIModel?) {
        errorCollector.apply{
            checkThat(resourceState.data, equalTo(bookFeedUIModel?.items))
            checkThat(resourceState.error, equalTo(bookFeedUIModel?.error))
            checkThat(resourceState.loading, equalTo(bookFeedUIModel?.loading))
        }
    }

    private fun initViewModelAndTestObserver() {
        bookFeedViewModel = BookFeedViewModel(bookRepository, testCoroutineDispatcher)
        liveDataTestObserver = bookFeedViewModel.uiModelLiveData.createTestObserver()
    }
}