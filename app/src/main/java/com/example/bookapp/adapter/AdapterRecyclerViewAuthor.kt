package com.example.bookapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.eightbitlab.shadowview.RenderScriptBlur
import com.eightbitlab.shadowview.RenderScriptProvider
import com.example.bookapp.databinding.ItemBookBinding
import com.example.bookapp.databinding.ItemCategoriesBinding
import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.models.Book
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterRecyclerViewAuthor(val author: (String) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerViewAuthor.ViewHolder>() {

    var list = ArrayList<Book>()
    var listDataBase = ArrayList<BookEntity>()

    fun addListBook(list: List<Book>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addDataBaseData(listDataBase: List<BookEntity>) {
        this.listDataBase.addAll(listDataBase)
        notifyDataSetChanged()
    }

    inner class ViewHolder(var itemBinding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(book: Book) {
            itemBinding.apply {
                title.setText(book.author)
            }
        }

        fun omBindDataBase(book: BookEntity) {
            itemBinding.apply {
                title.setText(book.auth)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoriesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list.isNotEmpty()) {
            holder.onBind(list[position])
        } else {
            holder.omBindDataBase(listDataBase.get(position))
        }

        holder.itemView.setOnClickListener {
            author.invoke(listDataBase.get(position).auth)
        }


    }

    override fun getItemCount(): Int {
        if (list.isNotEmpty()) {
            return list.size
        }

        return listDataBase.size

    }

}