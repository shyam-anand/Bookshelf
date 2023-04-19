package com.shyamanand.bookshelf.ui.state

data class SearchbarState(
    val searchString: String
) {
    companion object {
        val Empty: SearchbarState = SearchbarState(
            searchString = ""
        )
    }
}