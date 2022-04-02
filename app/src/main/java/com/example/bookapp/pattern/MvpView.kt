package com.example.bookapp.pattern

import com.example.bookapp.model.models.Book
import com.example.bookapp.model.models.MyBook
import com.example.bookapp.resource.BookResource

interface MvpView {

    fun loadingBook(myResosity:BookResource)
    fun showProgressbar()
    fun hideProgressbar()
}