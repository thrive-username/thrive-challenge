package com.triveglobal.challenge.datasource.remote

import com.triveglobal.challenge.model.Book
import retrofit2.http.*

internal interface BackendAPI {

    @POST("books")
    suspend fun saveBook(@Body book: Book): Book

    @DELETE("books/{id}")
    suspend fun deleteBook(@Path("id") id: Long)

    @DELETE("clean")
    suspend fun deleteAllBooks()

    @GET("books")
    suspend fun getAllBooks(): List<Book>

    @PUT("books/{id}")
    suspend fun updateBook(@Path("id") id: Long,@Body book: Book)


}