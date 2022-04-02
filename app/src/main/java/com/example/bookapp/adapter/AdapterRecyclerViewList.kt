package com.example.bookapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.eightbitlab.shadowview.RenderScriptBlur
import com.eightbitlab.shadowview.RenderScriptProvider
import com.example.bookapp.adapter.resource.BookAllDataResource
import com.example.bookapp.databinding.ItemBookBinding
import com.example.bookapp.databinding.ItemListBinding
import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.models.Book
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterRecyclerViewList(val bookClick: (BookEntity) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerViewList.ViewHolder>() {

    var list = ArrayList<Book>()
    var listDataBase = ArrayList<BookEntity>()

    fun addListBook(list: List<Book>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addDataBaseLis(list: List<BookEntity>) {
        this.listDataBase.clear()
        this.listDataBase.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(var itemBinding: ItemListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(book: Book) {
            itemBinding.apply {
                Picasso.get().load(book.book_image).into(image)
                title.setText(book.title)
                contributor.setText(book.contributor)
            }
        }

        fun onBindDataBase(book: BookEntity) {
            itemBinding.apply {
                image.setImageURI(Uri.fromFile(File(book.image)))
                title.setText(book.title)
                contributor.setText(book.contributor)
                description.setText(book.description)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!list.isEmpty()) {
            holder.onBind(list[position])
        } else {
            holder.onBindDataBase(listDataBase.get(position))
        }

        holder.itemView.setOnClickListener {
            bookClick.invoke(listDataBase.get(position))
//            if (list.isNotEmpty()) {
//                bookClick.invoke(BookAllDataResource.NetWorkData(list.get(position)), position)
//            } else {
//                bookClick.invoke(
//                    BookAllDataResource.DataBaseData(listDataBase.get(position)),
//                    position
//                )
//            }
        }

    }

    override fun getItemCount(): Int {
        if (!list.isEmpty()) {
            return list.size
        }
        return listDataBase.size
    }

}