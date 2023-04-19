package com.shyamanand.bookshelf.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "books")
data class Book(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val subtitle: String = "",

    val authors: List<String> = listOf(),

    @kotlinx.serialization.Transient
    val description: String = "",

    val categories: List<String> = listOf(),

    val imageLinks: ImageLinks = ImageLinks(),

    @kotlinx.serialization.Transient
    val averageRating: Int = 0,

    @kotlinx.serialization.Transient
    val ratingsCount: Int = 0
)
