package com.example.bookapp.adapter.resource

import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.models.Book
import com.example.bookapp.model.models.MyBook
import java.io.Serializable

sealed class BookAllDataResource() : Serializable {

    data class NetWorkData(val booksData: Book) : BookAllDataResource()
    data class DataBaseData(val booksData: BookEntity) : BookAllDataResource()
}
