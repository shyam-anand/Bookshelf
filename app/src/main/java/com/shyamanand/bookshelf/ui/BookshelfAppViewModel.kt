package com.shyamanand.bookshelf.ui

import androidx.lifecycle.ViewModel
import com.shyamanand.bookshelf.ui.state.AppUiState
import com.shyamanand.bookshelf.ui.screens.BookShelfAppScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookshelfAppViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState.HomeScreen)
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun setBookDetailsScreen(bookId: String) {
        _uiState.update { currentState ->
            currentState.copy(screen = BookShelfAppScreen.BookDetails, bookId = bookId)
        }
    }
}
