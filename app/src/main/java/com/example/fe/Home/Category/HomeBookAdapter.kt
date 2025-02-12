package com.example.app.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.BookLetter.LetterActivity
import com.example.fe.Home.Category.HomeBook
import com.example.fe.R

class HomeBookAdapter(private val bookList: List<HomeBook>) :
    RecyclerView.Adapter<HomeBookAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookCover: ImageView = view.findViewById(R.id.book_cover)
//        val bookTitle: TextView = view.findViewById(R.id.book_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_image, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
//        holder.bookCover.setImageResource(book.imageResId)
        Glide.with(holder.itemView.context)
            .load(R.drawable.book_sample1)
            .into(holder.bookCover)

//        holder.bookTitle.text = book.title

        // ⭐ 책 클릭 이벤트 추가 (북레터 화면으로 이동)
        holder.bookCover.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, LetterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = bookList.size
}
