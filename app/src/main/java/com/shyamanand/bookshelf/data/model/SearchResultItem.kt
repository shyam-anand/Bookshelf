package com.shyamanand.bookshelf.data.model

@kotlinx.serialization.Serializable
data class SearchResultItem(
    val id: String,
    val selfLink: String,
    val volumeInfo: Book
)
