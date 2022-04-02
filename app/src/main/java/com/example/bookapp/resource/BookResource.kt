package com.example.bookapp.resource

import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.models.MyBook

sealed class BookResource {

    object Loading : BookResource()
//    data class Succes(val myBook: MyBook) : BookResource()
    data class SuccesDataBase(val myBook: List<BookEntity>) : BookResource()
    data class Error(val message: String) : BookResource()
}