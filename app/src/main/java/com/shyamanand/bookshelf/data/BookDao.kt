package com.shyamanand.bookshelf.data

import androidx.room.Dao
import androidx.room.Insert
import com.shyamanand.bookshelf.data.model.Book

@Dao
interface BookDao {

    @Insert
    suspend fun insert(book: Book)


}