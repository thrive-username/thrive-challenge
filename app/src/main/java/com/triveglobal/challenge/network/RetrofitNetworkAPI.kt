package com.triveglobal.challenge.network

import com.triveglobal.challenge.di.qualifiers.BackendBaseUrl
import com.triveglobal.challenge.model.Book
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class RetrofitNetworkAPI @Inject constructor(@BackendBaseUrl private val backendBaseUrl: String) : NetworkAPI{

    private val backendAPI  by lazy {
        Retrofit.Builder()
            .baseUrl(backendBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendAPI::class.java)
    }

    override suspend fun addBook(book: Book) {
        backendAPI.addBook(book)
    }

    override suspend fun deleteBook(book: Book) {
        book.id?.let { backendAPI.deleteBook(it) }
    }

    override suspend fun getAllBooks(): List<Book> {
        return backendAPI.getAllBooks()
    }

    override suspend fun updateBook(book: Book) {
        book.id?.let { backendAPI.updateBook(it, book) }
    }
}