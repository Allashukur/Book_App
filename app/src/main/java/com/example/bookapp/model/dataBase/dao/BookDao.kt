package com.example.bookapp.model.dataBase.dao

import androidx.room.*
import com.example.bookapp.model.dataBase.entity.BookEntity

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBookData(mybook: BookEntity)

    @Query("select * from BookEntity")
    fun getAllData(): List<BookEntity>

    @Update
    fun editSaveBook(mybook: BookEntity)

}