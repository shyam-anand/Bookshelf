package com.shyamanand.bookshelf.data.model

@kotlinx.serialization.Serializable
data class ImageLinks(
    val smallThumbnail: String = "",

    val thumbnail: String = "",

    val small: String = "",

    val medium: String = "",

    val large: String = "",

    val extraLarge: String = ""
)
