package com.example.bookapp.model.models

import java.io.Serializable

data class Isbn(
    val isbn10: String,
    val isbn13: String
) : Serializable