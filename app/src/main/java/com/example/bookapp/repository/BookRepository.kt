package com.example.bookapp.repository

import android.content.Context
import com.example.bookapp.model.data_base.AppDataBase
import com.example.bookapp.model.models.MyBook
import com.example.myweather.retrofit.ApiServis
import retrofit2.Response

open class BookRepository(private val apiServis: ApiServis, var context: Context) {
    suspend fun getBookDatas(apiKey: String): Response<MyBook> = apiServis.getData(apiKey)
    suspend fun RoomDataBase() = AppDataBase.getInstance(context).bookDao()

}