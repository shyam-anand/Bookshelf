package com.shyamanand.bookshelf.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.shyamanand.bookshelf.BookshelfApplication
import com.shyamanand.bookshelf.TAG
import com.shyamanand.bookshelf.data.BooksRepository
import com.shyamanand.bookshelf.ui.state.BookDetailsScreenState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BookDetailsScreenViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    var state: BookDetailsScreenState by mutableStateOf(BookDetailsScreenState.Loading)

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            Log.d(TAG, "loading $bookId")
            state = BookDetailsScreenState.Loading

            state = try {
                val book = booksRepository.getDetails(bookId)
                Log.i(TAG, "Found book: ${book.title}")
                BookDetailsScreenState.Success(bookId, book)
            } catch (e: IOException) {
                Log.e(TAG, "IOException: " + e.stackTraceToString())
                BookDetailsScreenState.Error
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: " + e.stackTraceToString())
                BookDetailsScreenState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksRepository = application.container.booksRepository
                BookDetailsScreenViewModel(booksRepository = booksRepository)
            }
        }
    }
}