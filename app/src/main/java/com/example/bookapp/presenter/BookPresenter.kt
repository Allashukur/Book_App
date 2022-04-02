package com.example.bookapp.presenter

import androidx.lifecycle.LiveData
import com.example.bookapp.resource.BookResource

interface BookPresenter {

    fun loadNetworkBookData()

    fun loadDataBaseBookData()

}