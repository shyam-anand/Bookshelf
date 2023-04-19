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
import com.shyamanand.bookshelf.data.BooksRepository
import com.shyamanand.bookshelf.ui.state.ResultsGridState
import com.shyamanand.bookshelf.ui.state.SearchbarState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val HOME_SCREEN_VIEW_MODEL = "HomeScreenViewModel"

class HomeScreenViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksRepository = application.container.booksRepository
                HomeScreenViewModel(booksRepository = booksRepository)
            }
        }
    }

    var searchbarState: SearchbarState by mutableStateOf(SearchbarState.Empty)
        private set

    var resultsGridState: ResultsGridState by mutableStateOf(ResultsGridState.Loading)
        private set

    private fun search(title: String) {
        viewModelScope.launch {
            resultsGridState = ResultsGridState.Loading

            resultsGridState = try {
                val searchResult = booksRepository.search(title)
                Log.i(
                    HOME_SCREEN_VIEW_MODEL,
                    "Found ${searchResult.totalItems} results for title $title"
                )
                if (searchResult.totalItems > 0) {
                    ResultsGridState.Success(searchResult)
                } else {
                    ResultsGridState.Loading
                }
            } catch (e: IOException) {
                Log.e(HOME_SCREEN_VIEW_MODEL, e.stackTraceToString())
                ResultsGridState.Error
            } catch (e: HttpException) {
                Log.e(HOME_SCREEN_VIEW_MODEL, e.stackTraceToString())
                ResultsGridState.Error
            }
        }
    }

    fun onSearchbarInput(searchString: String) {
        searchbarState = SearchbarState(searchString)
        if (searchString.length > 3) {
            Log.i(HOME_SCREEN_VIEW_MODEL, "searchString=$searchString")
            search(searchString)
        } else {
            resultsGridState = ResultsGridState.Loading
        }
    }

    fun search() {
        search(searchbarState.searchString)
    }
}