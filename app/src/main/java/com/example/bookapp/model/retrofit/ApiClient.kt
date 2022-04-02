package com.example.myweather.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {

    val apiServis = getRetrofit().create(ApiServis::class.java)

    const val BASE_URL =
        "https://api.nytimes.com/"


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}