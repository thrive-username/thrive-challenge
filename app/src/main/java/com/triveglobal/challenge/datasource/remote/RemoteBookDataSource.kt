package com.triveglobal.challenge.datasource.remote

import com.triveglobal.challenge.model.Book

interface RemoteBookDataSource {

    suspend fun saveBook(book: Book): Book

    suspend fun deleteBook(book: Book)

    suspend fun deleteAllBooks()

    suspend fun getAllBooks(): List<Book>

    suspend fun updateBook(book: Book)

}