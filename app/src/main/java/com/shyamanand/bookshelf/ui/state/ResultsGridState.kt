package com.shyamanand.bookshelf.ui.state

import com.shyamanand.bookshelf.data.model.SearchResult

sealed interface ResultsGridState {
    data class Success(val searchResult: SearchResult) : ResultsGridState

    object Error : ResultsGridState

    object Loading: ResultsGridState
}