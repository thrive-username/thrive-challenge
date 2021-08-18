package com.triveglobal.challenge.datasource.local

import com.triveglobal.challenge.model.Book
import kotlinx.coroutines.flow.Flow

interface LocalBookDataSource {

    suspend fun saveOrUpdateBook(book: Book)

    suspend fun deleteBook(book: Book)

    suspend fun getAllBooks(): List<Book>

}