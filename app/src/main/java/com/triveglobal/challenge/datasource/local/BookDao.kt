package com.triveglobal.challenge.datasource.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface BookDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveOrUpdateBook(bookEntity: BookEntity)

    @Delete
    suspend fun deleteBook(bookEntity: BookEntity)

    @Query("delete from book")
    suspend fun deleteAllBooks()

    @Query("select * from book")
    suspend fun getAllBooks(): List<BookEntity>

    @Update
    suspend fun updateBook(bookEntity: BookEntity)

}