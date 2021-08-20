package com.triveglobal.challenge.datasource.local

import com.triveglobal.challenge.BookMockHelper
import com.triveglobal.challenge.extensions.parseDateTime
import com.triveglobal.challenge.extensions.serializeToString
import com.triveglobal.challenge.model.Book
import org.hamcrest.CoreMatchers.equalTo
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import java.lang.IllegalArgumentException

class BookEntityTest {

    @get:Rule
    val errorCollector = ErrorCollector()

    @Test(expected = IllegalArgumentException::class)
    fun `toEntity without Id, throws IllegalArgumentException`() {
        val book = BookMockHelper.createBook(id = null)
        BookEntity.toEntity(book)
    }

    @Test
    fun `toEntity with Id, bookEntity created`() {
        val book = BookMockHelper.createBook(id = 155, DateTime.now(), "John Doe")
        val bookEntity = BookEntity.toEntity(book)
        errorCollector.apply {
            checkThat(book.author, equalTo(bookEntity.author))
            checkThat(book.categories, equalTo(bookEntity.categories))
            checkThat(book.id, equalTo(bookEntity.id))
            checkThat(book.lastCheckedOutBy, equalTo(bookEntity.lastCheckedOutBy))
            checkThat(book.lastCheckedOut?.serializeToString(), equalTo(bookEntity.lastCheckedOut))
            checkThat(book.title, equalTo(bookEntity.title))
            checkThat(book.publisher, equalTo(bookEntity.publisher))
        }
    }

    @Test
    fun `toBook, book created`() {
        val bookEntity = BookEntity(
            "author1",
            "categories1",
            1234,
            "2021-08-20 15:49:36 UTC",
            "John Doe",
            "publisher1",
            "title1"
        )
        val book = bookEntity.toBook()
        errorCollector.apply {
            checkThat(bookEntity.author, equalTo(book.author))
            checkThat(bookEntity.categories, equalTo(book.categories))
            checkThat(bookEntity.id, equalTo(book.id))
            checkThat(bookEntity.lastCheckedOutBy, equalTo(book.lastCheckedOutBy))
            checkThat(bookEntity.lastCheckedOut?.parseDateTime(), equalTo(DateTime(2021, 8,  20, 15, 49, 36, DateTimeZone.UTC)))
            checkThat(bookEntity.title, equalTo(book.title))
            checkThat(bookEntity.publisher, equalTo(book.publisher))
        }
    }

}