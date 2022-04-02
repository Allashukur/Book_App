package com.example.bookapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eightbitlab.shadowview.RenderScriptBlur
import com.eightbitlab.shadowview.RenderScriptProvider
import com.example.bookapp.R
import com.example.bookapp.adapter.AdapterRecyclerView
import com.example.bookapp.adapter.AdapterRecyclerViewAuthor
import com.example.bookapp.adapter.resource.BookAllDataResource
import com.example.bookapp.databinding.FragmentHomeBinding
import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.data_base.AppDataBase
import com.example.bookapp.pattern.MvpView
import com.example.bookapp.presenter.BookPresenterImpl
import com.example.bookapp.resource.BookResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.BufferUnderflowException
import kotlin.coroutines.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), MvpView, CoroutineScope {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var bookPresenterImpl: BookPresenterImpl
    lateinit var binding: FragmentHomeBinding
    lateinit var adapterRecyclerView: AdapterRecyclerView
    lateinit var adapterRecyclerViewFavorite: AdapterRecyclerView
    lateinit var adapterRecyclerViewAuthor: AdapterRecyclerViewAuthor
    lateinit var appDataBase: AppDataBase

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)



        loadPresenterAndViews()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadPresenterAndViews() {
        Log.d("LOG777", "loadPresenterAndViews")

        appDataBase = AppDataBase.getInstance(requireContext())
        bookPresenterImpl = BookPresenterImpl(this, requireContext(), requireActivity())
        bookPresenterImpl.loadBookAllData()

        val window: Window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
        val renderScriptProvider = RenderScriptProvider(requireContext())
        binding.shadowView.blurScript = RenderScriptBlur(renderScriptProvider)
        binding.shadowView2.blurScript = RenderScriptBlur(renderScriptProvider)

        adapterRecyclerViewFavorite = AdapterRecyclerView {
            var bundle = Bundle()
            bundle.putSerializable("book", it)
            findNavController().navigate(R.id.infoFragment, bundle)
        }

//        bookPresenterImpl.loadNetworkBookData()
        adapterRecyclerView = AdapterRecyclerView() { book ->
            var bundle = Bundle()
            bundle.putSerializable("book", book)
            findNavController().navigate(R.id.infoFragment, bundle)
        }
        adapterRecyclerViewAuthor = AdapterRecyclerViewAuthor() {
            var bundle = Bundle()
            bundle.putString("auth", it)
            findNavController().navigate(R.id.listFragment, bundle)
        }

        binding.apply {
            menuBtn.setOnClickListener {
                Toast.makeText(requireContext(), "Menu", Toast.LENGTH_SHORT).show()
            }
            searchButton.setOnClickListener {
                findNavController().navigate(R.id.listFragment)
            }
            rvBook.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvAuthor.layoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
            rvBook2.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvBook2.adapter = adapterRecyclerViewFavorite
            rvBook.adapter = adapterRecyclerView
            rvAuthor.adapter = adapterRecyclerViewAuthor
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun loadingBook(myResosity: BookResource) {
        Log.d("LOG777", "loadingBook")

        val allData = appDataBase.bookDao().getAllData()

        launch {
            val listFav: List<BookEntity> = allData.filter { s -> s.save_page == true }
            if (listFav.isEmpty()) {
                binding.apply {
//                    shadowView2.layoutParams.height = 0
                    favoriteTitle.visibility = View.GONE
                    shadowView2.visibility = View.GONE
                }

            }
            adapterRecyclerViewFavorite.addDataBaseLis(listFav)
            when (myResosity) {
                is BookResource.Loading -> {
                    Log.d("Def77", "HOME Loading...")
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()

                }

                is BookResource.Error -> {
                    Log.d("Def77", " HOME Error")
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
                is BookResource.SuccesDataBase -> {
                    Log.d("Def77", "HOME ${myResosity.myBook}")
                    adapterRecyclerViewAuthor.addDataBaseData(myResosity.myBook)
                    adapterRecyclerView.addDataBaseLis(myResosity.myBook)

                    adapterRecyclerView.notifyDataSetChanged()
                    adapterRecyclerViewAuthor.notifyDataSetChanged()


                }
            }
//        })
        }


    }

    override fun showProgressbar() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
//        adapterRecyclerViewAuthor.notifyDataSetChanged()
//        adapterRecyclerView.notifyDataSetChanged()
        binding.progress.visibility = View.GONE
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}