package com.shyamanand.bookshelf.data

import android.util.Log
import com.shyamanand.bookshelf.TAG
import com.shyamanand.bookshelf.data.model.Book
import com.shyamanand.bookshelf.data.model.SearchResult
import com.shyamanand.bookshelf.network.GoogleApiService

interface BooksRepository {

    suspend fun search(title: String): SearchResult

    suspend fun getDetails(id: String): Book
}

class GoogleBooksRepository(
    private val googleApiService: GoogleApiService
) : BooksRepository {

    override suspend fun getDetails(id: String): Book {
        Log.i(TAG, "getDetails: $id")
        return googleApiService.getDetails(id).volumeInfo
    }

    override suspend fun search(title: String): SearchResult {
        Log.i(TAG, "search: $title")
        return googleApiService.search(title)
    }
}

