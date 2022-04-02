package com.example.bookapp.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eightbitlab.shadowview.RenderScriptBlur
import com.eightbitlab.shadowview.RenderScriptProvider
import com.example.bookapp.R
import com.example.bookapp.adapter.resource.BookAllDataResource
import com.example.bookapp.databinding.FragmentInfoBinding
import com.example.bookapp.model.dataBase.entity.BookEntity
import com.example.bookapp.model.data_base.AppDataBase
import com.example.bookapp.model.models.Book
import com.squareup.picasso.Picasso
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
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

    lateinit var binding: FragmentInfoBinding
    lateinit var appDataBase: AppDataBase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        binding.apply {
            playBook.setOnClickListener {
                Toast.makeText(requireContext(), "Play Book", Toast.LENGTH_SHORT).show()
            }
            readBook.setOnClickListener {
                Toast.makeText(requireContext(), "Read Book", Toast.LENGTH_SHORT).show()
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }


        }

        loadViewAndData()


        return binding.root
    }

    private fun loadViewAndData() {
        val window: Window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
        val renderScriptProvider = RenderScriptProvider(requireContext())
        appDataBase = AppDataBase.getInstance(requireContext())
        binding.shadowView.blurScript = RenderScriptBlur(renderScriptProvider)

        setData()


    }

    private fun setData() {

        val serializable = arguments?.getSerializable("book")
        val serializableData = arguments?.getSerializable("auth")
        if (serializable != null) {
            val book_data: BookEntity = serializable as BookEntity
            setInfoData(book_data)
        }

        if (serializableData != null) {
            val bookData: BookEntity = serializableData as BookEntity
            setInfoData(bookData)
        }


    }

    private fun setInfoData(bookEntity: BookEntity) {
        binding.apply {
            if (bookEntity.save_page) {
                saveBook.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }
            image.setImageURI(Uri.fromFile(File(bookEntity.image)))
            titleTv.setText(bookEntity.title.toString())
            contributorTv.setText(bookEntity.contributor.toString())
            priceTv.setText("${bookEntity.price}")
            rankTv.setText(bookEntity.rank.toString())
            publisherTv.setText(bookEntity.publisher.toString())
            descriptionTv.setText(bookEntity.description.toString())
            urlText.setText("Amazon book market : ${bookEntity.Amazon_url} \n\n Apple book market : ${bookEntity.Apple_url}")

            saveBook.setOnClickListener {

                if (!bookEntity.save_page) {
                    bookEntity.save_page = true
                    saveBook.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    appDataBase.bookDao().editSaveBook(bookEntity)
                } else {
                    bookEntity.save_page = false
                    appDataBase.bookDao().editSaveBook(bookEntity)
                    saveBook.setImageResource(R.drawable.ic_baseline_bookmark_border_24)

                }
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}