package com.shyamanand.bookshelf.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.shyamanand.bookshelf.data.AppContainer
import com.shyamanand.bookshelf.data.BooksRepository
import com.shyamanand.bookshelf.data.GoogleBooksRepository
import com.shyamanand.bookshelf.data.model.SearchResult
import com.shyamanand.bookshelf.data.model.SearchResultItem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class DefaultAppContainer : AppContainer {

    private val googleBooksBaseUrl = "https://www.googleapis.com/books/v1/"

    private val responseFormat = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    private val jsonConverterFactory = responseFormat
        .asConverterFactory("application/json".toMediaType())

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(jsonConverterFactory)
        .baseUrl(googleBooksBaseUrl)
        .build()

    private val retrofitService : GoogleApiService by lazy {
        retrofit.create(GoogleApiService::class.java)
    }

    override val booksRepository: BooksRepository by lazy {
        GoogleBooksRepository(retrofitService)
    }

}

interface GoogleApiService {

    @GET("volumes")
    suspend fun search(
        @Query(value = "q", encoded = true) title: String): SearchResult

    @GET("volumes/{id}")
    suspend fun getDetails(@Path("id") id: String): SearchResultItem
}