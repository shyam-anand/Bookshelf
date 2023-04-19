package com.shyamanand.bookshelf.ui.state

import com.shyamanand.bookshelf.ui.screens.BookShelfAppScreen

data class AppUiState(
    val screen: BookShelfAppScreen = BookShelfAppScreen.Home,
    val bookId: String? = null
) {
    companion object {
        val HomeScreen: AppUiState = AppUiState(BookShelfAppScreen.Home)
    }
}
