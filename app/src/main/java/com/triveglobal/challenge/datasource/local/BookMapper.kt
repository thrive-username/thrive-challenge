package com.triveglobal.challenge.datasource.local

import com.triveglobal.challenge.extensions.deserializeToDateTime
import com.triveglobal.challenge.extensions.serializeToString
import com.triveglobal.challenge.model.Book
import java.lang.IllegalArgumentException

object BookMapper {

    fun toEntity(book: Book) = book.run {
        if (id == null) throw IllegalArgumentException("A book entity requires a non-null ID")
        BookEntity(author, categories, id, lastCheckedOut.serializeToString(), lastCheckedOutBy, publisher, title)
    }

    fun toBook(bookEntity: BookEntity) = bookEntity.run {
        Book(author, categories, id, lastCheckedOutBy.deserializeToDateTime(), lastCheckedOutBy, publisher, title)
    }

}