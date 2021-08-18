package com.triveglobal.challenge.datasource.local

import android.content.Context
import com.triveglobal.challenge.di.qualifiers.ApplicationContext
import com.triveglobal.challenge.model.Book
import javax.inject.Inject

class RoomLocalBookDataSource @Inject constructor(@ApplicationContext private val context: Context) : LocalBookDataSource{

    private val bookDao by lazy { ChallengeDatabase.getDatabase(context).bookDao() }

    override suspend fun saveOrUpdateBook(book: Book) {
        bookDao.saveOrUpdateBook(BookMapper.toEntity(book))
    }

    override suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(BookMapper.toEntity(book))
    }

    override suspend fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks().map { BookMapper.toBook(it) }
    }

    override suspend fun updateBook(book: Book) {
        bookDao.deleteBook(BookMapper.toEntity(book))
    }


}