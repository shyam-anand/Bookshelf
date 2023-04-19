package com.shyamanand.bookshelf.ui.state

import com.shyamanand.bookshelf.data.model.Book

sealed interface BookDetailsScreenState{
    data class Success(val bookId: String, val book: Book) : BookDetailsScreenState
    object Error: BookDetailsScreenState
    object Loading: BookDetailsScreenState
}