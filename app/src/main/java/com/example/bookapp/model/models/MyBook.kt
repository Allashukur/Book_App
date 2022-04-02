package com.example.bookapp.model.models

import java.io.Serializable

data class MyBook(
    val copyright: String,
    val last_modified: String,
    val num_results: Int,
    val results: Results,
    val status: String
) : Serializable