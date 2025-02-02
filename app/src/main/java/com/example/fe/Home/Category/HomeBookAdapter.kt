package com.example.fe.Home.Category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

class HomeBookAdapter(private val bookList: List<HomeBook>) :
    RecyclerView.Adapter<HomeBookAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookCover: ImageView = view.findViewById(R.id.book_cover)
        val bookTitle: TextView = view.findViewById(R.id.book_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_letter, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookCover.setImageResource(book.imageResId)
        holder.bookTitle.text = book.title
    }

    override fun getItemCount(): Int = bookList.size
}