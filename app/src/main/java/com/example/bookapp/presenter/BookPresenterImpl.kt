package com.example.bookapp.presenter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.data_base.AppDataBase
import com.example.bookapp.model.models.MyBook
import com.example.bookapp.pattern.MvpView
import com.example.bookapp.repository.BookRepository
import com.example.bookapp.resource.BookResource
import com.example.bookapp.servis.DownloadImage
import com.example.myweather.retrofit.ApiClient
import com.example.myweather.retrofit.ApiServis
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookPresenterImpl(
    val mvpView: MvpView,
    val context: Context,
    val activity: ComponentActivity
) : BookPresenter, ViewModel(), DataBaseCallback {

    private val repository = BookRepository(ApiClient.apiServis, context)
    lateinit var appDataBase: AppDataBase

    //    var liveData = MutableLiveData<BookResource>(BookResource.Loading)
    var imageurl: String? = null


    @RequiresApi(Build.VERSION_CODES.M)
    fun loadBookAllData() {
        mvpView.showProgressbar()
        if (isOnline(context)) {
            loadNetworkBookData()
        } else {
            loadDataBaseBookData()
        }
    }

    override fun loadNetworkBookData() {
        appDataBase = AppDataBase.getInstance(context)
//        downloadImage.verifyPermission(context, activity)

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        try {
            val download = DownloadImage(this)

            viewModelScope.launch(coroutineExceptionHandler) {
                coroutineScope {
                    val mybook = repository.getBookDatas("3Nz26qV5QwAfGBOAAET5GPAB6ozTtA2A")
                    if (mybook.isSuccessful) {
//                        Log.d("LOG777", "${mybook.body()}")
//                        mvpView.loadingBook(
////                            BookResource.Succes(
////                                (mybook.body() ?: emptyList<MyBook>()) as MyBook
////                            )
//                        )


                        for (i in mybook.body()?.results?.books!!) {
                            var size_list = mybook.body()!!.results.books.size-1
                            download.downloadImage(i.book_image, context, activity, i, size_list)

//                            Log.d("List777", "NetWork  =  $i")
//                            Log.d("List777", "Image Url =  $imageurl")
//                            if (imageurl != null) {
//                                Log.d("List777", "Image Url IF Metod =  $imageurl")
//                                repository.RoomDataBase().addBookData(
//                                    BookEntity(
//                                        0,
//                                        i.title,
//                                        i.description,
//                                        i.price,
//                                        i.rank.toString(),
//                                        i.publisher,
//                                        imageurl.toString(),
//                                        i.contributor,
//                                        i.author,
//                                        false,
//                                        i.amazon_product_url,
//                                        i.buy_links.get(1).url
//                                    )
//                                )
//                            }
                        }

//                        val allData = appDataBase.bookDao().getAllData()
//                        if (allData.isNotEmpty()) {
//                            mvpView.hideProgressbar()
//                            mvpView.loadingBook(
//                                BookResource.SuccesDataBase(
//                                    appDataBase.bookDao().getAllData()
//                                )
//                            )
//                        }
                        loadDataBaseBookData()

//                        liveData.postValue(mybook.body()?.let { BookResource.Succes(it) })

                    }
                }
            }
//            Log.d("List777", "SAVE ROOM =  $allData")

        } catch (e: Exception) {
            mvpView.loadingBook(BookResource.Error(e.message.toString()))
        }

//        val allData = appDataBase.bookDao().getAllData()
//        if (allData.isNotEmpty()) {
//            mvpView.loadingBook(BookResource.SuccesDataBase(allData))
//            mvpView.hideProgressbar()
//        }

    }

    override fun loadDataBaseBookData() {
        try {
            viewModelScope.launch {
                coroutineScope {
                    val allRoomData = repository.RoomDataBase().getAllData()

                    Log.d("Room", "$allRoomData")
                    if (allRoomData.isEmpty()) {
                        mvpView.loadingBook(BookResource.Error("List empt"))
                    } else {
                        mvpView.hideProgressbar()
                        mvpView.loadingBook(BookResource.SuccesDataBase(allRoomData))
                    }

                }
            }

        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun SuccessfulData() {
        loadDataBaseBookData()
    }

//    override fun callImageUrl(imageUrl: String) {
//        imageurl = imageUrl
//        Log.d("Download777", "callImageUrl : ${imageurl}")
//
//    }


}