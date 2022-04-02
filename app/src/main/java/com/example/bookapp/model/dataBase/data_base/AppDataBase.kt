package com.example.bookapp.model.data_base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookapp.model.dataBase.dao.BookDao
import com.example.bookapp.model.dataBase.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        private var instanse: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            if (instanse == null) {
                instanse = Room.databaseBuilder(context, AppDataBase::class.java, "BookApp")
                    .allowMainThreadQueries().build()
            }
            return instanse!!
        }
    }
}