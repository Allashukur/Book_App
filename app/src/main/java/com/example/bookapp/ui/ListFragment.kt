package com.example.bookapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.adapter.AdapterRecyclerViewAuthor
import com.example.bookapp.adapter.AdapterRecyclerViewList
import com.example.bookapp.databinding.FragmentListBinding
import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.data_base.AppDataBase
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : Fragment() {


    lateinit var binding: FragmentListBinding
    lateinit var appDataBase: AppDataBase
    lateinit var adapterRecyclerViewList: AdapterRecyclerViewList
    lateinit var tempList: ArrayList<BookEntity>
    lateinit var list: ArrayList<BookEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        list = ArrayList()
        tempList = ArrayList()

        binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            searchButton.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    tempList.clear()
                    val query_text = p0?.lowercase(Locale.getDefault())
                    Log.d("List777", "clear  =  $tempList")
                    if (!query_text.isNullOrEmpty()) {
                        list.forEach {
                            if (it.title.lowercase(Locale.getDefault()).contains(query_text)) {
                                Log.d("List777", " for = $it")
                                tempList.add(it)
                            }
                        }
                        Log.d("List777", " adapter = $tempList")
                        adapterRecyclerViewList.addDataBaseLis(tempList)
                        rv.adapter?.notifyDataSetChanged()
                        binding.rv.adapter = adapterRecyclerViewList
                    }
                    else {
                        adapterRecyclerViewList.addDataBaseLis(list)
                        binding.rv.adapter = adapterRecyclerViewList
                        rv.adapter?.notifyDataSetChanged()
                    }

                    return false
                }

            })
        }
        loadData()


        return binding.root
    }

    private fun loadData() {
        val auth = arguments?.getString("auth")
        adapterRecyclerViewList = AdapterRecyclerViewList() {
            var bundle = Bundle()
            bundle.putSerializable("auth", it)
            findNavController().navigate(R.id.infoFragment, bundle)
        }
        appDataBase = AppDataBase.getInstance(requireContext())
        list = appDataBase.bookDao().getAllData() as ArrayList<BookEntity>
        tempList.addAll(list)
        if (auth != null) {
            val data = ArrayList<BookEntity>()
//            val allDataDataBase = appDataBase.bookDao().getAllData()
            for (i in list) {
                if (i.auth.equals(auth)) {
                    data.add(i)
                }
            }
            adapterRecyclerViewList.addDataBaseLis(data)
        } else {
            adapterRecyclerViewList.addDataBaseLis(list)
        }
        binding.rv.adapter = adapterRecyclerViewList


    }


}