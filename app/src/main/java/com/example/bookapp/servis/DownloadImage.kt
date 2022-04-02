package com.example.bookapp.servis

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.core.app.ComponentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.data_base.AppDataBase
import com.example.bookapp.model.models.Book
import com.example.bookapp.pattern.MvpView
import com.example.bookapp.presenter.BookPresenterImpl
import com.example.bookapp.presenter.DataBaseCallback
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class DownloadImage(var dataBaseCallback: DataBaseCallback) {
    var imageUrl: String? = null
    lateinit var appDataBase: AppDataBase
    var listSize: Int = -1
    var count = -1


    public fun downloadImage(
        imageUrl: String,
        context: Context,
        activity: ComponentActivity,
        book: Book, listSizes: Int
    ) {
        appDataBase = AppDataBase.getInstance(context)
//        var isImage = true
//        var file = File("/data/user/0/com.example.bookapp/cache/")
//        val listFiles = file.listFiles()
        val allData = appDataBase.bookDao().getAllData()
        Log.d("Def77", "first room data  : $allData")

        listSize = listSizes

        if (allData.isEmpty()) {
            downloadImage(activity, context, book)
        } else {
            for (i in appDataBase.bookDao().getAllData()) {
                if (!i.title.equals(book.title)) {
                    downloadImage(activity, context, book)
                }
            }
        }

//        if (!appDataBase.bookDao().getAllData().isEmpty()) {
//            for (i in listFiles) {
//                if (i.name.length > 6) {
//                    val file_name = i.name.substring(0, i.name.length - 4)
//                    if (file_name.equals(bookTitle)) {
//                        isImage = false
//                    }
//                }
//            }
//        }
//        for (i in appDataBase.bookDao().getAllData()) {
//            if (!i.title.equals(bookTitle)) {
//                isImage = true
//            }
//        }
//        if (isImage) {
//
//
//        }

    }


    private fun downloadImage(activity: ComponentActivity, context: Context, book: Book) {
        val dir = File(activity.cacheDir, book.title + ".jpg")

        Glide.with(context).load(book.book_image).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                val bitmap: Bitmap = (resource as BitmapDrawable).bitmap
                saveImage(bitmap, dir, context, book)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                Log.d("Def77", "Failed to Download Image! Please try again later.")

                Toast.makeText(
                    context,
                    "Failed to Download Image! Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun saveImage(imageBitmap: Bitmap?, storageDir: File, context: Context, book: Book) {
//        var successDirCreated = false

//        if (!storageDir.exists()) {
//        storageDir.mkdir()
//        }

        var isImage = true
        var file = File("/data/user/0/com.example.bookapp/cache/")
        val listFiles = file.listFiles()

        for (i in listFiles) {
            if (i.name.length > 6) {
                val file_name = i.name.substring(0, i.name.length - 4)
                if (file_name.equals(book.title)) {
                    isImage = false
                }
            }
        }

//        if (successDirCreated) {
//            val imageFile = File(storageDir, imageFileName)
//        val savedImagePath = storageDir.absolutePath

        if (isImage) {
            try {
                val fOut = FileOutputStream(storageDir)
                imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
//                Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                imageUrl = storageDir.absolutePath
                Log.d("Def77", "IMAGE URL  : $imageUrl")

                saveDataBase(book, imageUrl.toString())
//            imageCallUrl.callImageUrl(storageDir.absolutePath)
//            Log.d("Download", "$savedImagePath")
//            Log.d("Download777", "Image Saved!")


            } catch (e: Exception) {

                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
//        } else {
//            Log.d("Download", "Failed to make folder!")
//            Toast.makeText(context, "Failed to make folder!", Toast.LENGTH_SHORT).show();
//        }

    }

    private fun saveDataBase(book: Book, memoryUrl: String) {
        Log.d("Def77", "saveDataBase : $book $memoryUrl")

        appDataBase.bookDao().addBookData(
            BookEntity(
                0,
                book.title,
                book.description,
                book.price,
                book.rank.toString(),
                book.publisher,
                memoryUrl,
                book.contributor,
                book.author,
                false,
                book.amazon_product_url.toString(),
                book.buy_links[1].url
            )
        )
        count++
        if ((listSize != -1 && count != -1) && count == listSize) {
            dataBaseCallback.SuccessfulData()
        }
//        Log.d("Def77", "Saqlangan Datalar : ${appDataBase.bookDao().getAllData()}")
        Log.d("Def77", "Count : $count")
        Log.d("Def77", "list : $listSize")
    }

//    fun downloadImage(bookImage: String, context: Context, activity: ComponentActivity, title: String, i: Book) {
//
//    }

    /*  fun verifyPermission(context: Context, activity: ComponentActivity): Boolean {
          val permissionExternal = ActivityCompat.checkSelfPermission(
              context,
              Manifest.permission.WRITE_EXTERNAL_STORAGE
          )

          if (permissionExternal != PackageManager.PERMISSION_GRANTED) {
              val STORAGE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
              ActivityCompat.requestPermissions(activity, STORAGE_PERMISSIONS, 1)
              return false
          }
          return true
      }*/
}