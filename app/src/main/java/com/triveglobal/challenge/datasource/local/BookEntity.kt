package com.triveglobal.challenge.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.triveglobal.challenge.extensions.parseDateTime
import com.triveglobal.challenge.extensions.serializeToString
import com.triveglobal.challenge.model.Book
import java.lang.IllegalArgumentException

@Entity(tableName = "book")
data class BookEntity(@ColumnInfo val author: String,
                      @ColumnInfo val categories: String,
                      @PrimaryKey val id: Long,
                      @ColumnInfo val lastCheckedOut: String?,
                      @ColumnInfo val lastCheckedOutBy: String?,
                      @ColumnInfo val publisher: String,
                      @ColumnInfo val title: String) {

    companion object {
        fun toEntity(book: Book) = book.run {
            if (id == null) throw IllegalArgumentException("A book entity requires a non-null ID")
            BookEntity(author, categories, id, lastCheckedOut.serializeToString(), lastCheckedOutBy, publisher, title)
        }

        fun toBook(bookEntity: BookEntity) = bookEntity.run {
            Book(author, categories, id, lastCheckedOutBy.parseDateTime(), lastCheckedOutBy, publisher, title)
        }
    }

}