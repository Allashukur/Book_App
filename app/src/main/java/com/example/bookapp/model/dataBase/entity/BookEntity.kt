package com.example.bookapp.model.dataBase.entity

import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var description: String,
    var price: String,
    var rank: String,
    var publisher: String,
    var image: String,
    var contributor: String,
    var auth: String,
    var save_page: Boolean,
    var Amazon_url: String,
    var Apple_url: String
) : Serializable {

}