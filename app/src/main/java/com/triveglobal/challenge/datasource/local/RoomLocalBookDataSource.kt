package com.triveglobal.challenge.datasource.local

import android.content.Context
import com.triveglobal.challenge.datasource.local.BookEntity.Companion.toBook
import com.triveglobal.challenge.datasource.local.BookEntity.Companion.toEntity
import com.triveglobal.challenge.di.qualifiers.ApplicationContext
import com.triveglobal.challenge.model.Book
import javax.inject.Inject

class RoomLocalBookDataSource @Inject constructor(@ApplicationContext private val context: Context) : LocalBookDataSource{

    private val bookDao by lazy { ChallengeDatabase.getDatabase(context).bookDao() }

    override suspend fun saveOrUpdateBook(book: Book) {
        bookDao.saveOrUpdateBook(toEntity(book))
    }

    override suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(toEntity(book))
    }

    override suspend fun deleteAllBooks() {
        bookDao.deleteAllBooks()
    }

    override suspend fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks().map { toBook(it) }
    }


}