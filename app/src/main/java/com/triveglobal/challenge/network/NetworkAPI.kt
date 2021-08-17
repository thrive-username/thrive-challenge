package com.triveglobal.challenge.network

import com.triveglobal.challenge.model.Book

interface NetworkAPI {

    suspend fun addBook(book: Book)

    suspend fun deleteBook(book: Book)

    suspend fun getAllBooks(): List<Book>

    suspend fun updateBook(book: Book)

}