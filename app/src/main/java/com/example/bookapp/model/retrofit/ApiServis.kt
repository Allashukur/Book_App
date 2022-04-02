package com.example.myweather.retrofit

import com.example.bookapp.model.models.MyBook
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiServis {

    @GET("/svc/books/v3/lists/current/hardcover-fiction.json")
    suspend fun getData(@Query("api-key") apiKey: String): Response<MyBook>


}