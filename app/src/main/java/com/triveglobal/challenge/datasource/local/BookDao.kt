package com.triveglobal.challenge.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {

    @Update
    suspend fun saveOrUpdateBook(bookEntity: BookEntity)

    @Delete
    suspend fun deleteBook(bookEntity: BookEntity)

    @Query("select * from book")
    suspend fun getAllBooks(): List<BookEntity>

    @Update
    suspend fun updateBook(bookEntity: BookEntity)

}