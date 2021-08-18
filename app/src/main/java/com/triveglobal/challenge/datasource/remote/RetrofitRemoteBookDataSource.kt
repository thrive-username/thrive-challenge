package com.triveglobal.challenge.datasource.remote

import com.google.gson.GsonBuilder
import com.triveglobal.challenge.di.qualifiers.BackendBaseUrl
import com.triveglobal.challenge.model.Book
import org.joda.time.DateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient


class RetrofitRemoteBookDataSource @Inject constructor(@BackendBaseUrl private val backendBaseUrl: String) :
    RemoteBookDataSource  {

    private val backendAPI  by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder().registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter()).create()
        Retrofit.Builder()
            .baseUrl(backendBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BackendAPI::class.java)
    }

    override suspend fun saveBook(book: Book): Book {
        return backendAPI.saveBook(book)
    }

    override suspend fun deleteBook(book: Book) {
        book.id?.let { backendAPI.deleteBook(it) }
    }

    override suspend fun deleteAllBooks() {
        backendAPI.deleteAllBooks()
    }

    override suspend fun getAllBooks(): List<Book> {
        return backendAPI.getAllBooks()
    }

    override suspend fun updateBook(book: Book) {
        book.id?.let { backendAPI.updateBook(it, book) }
    }
}