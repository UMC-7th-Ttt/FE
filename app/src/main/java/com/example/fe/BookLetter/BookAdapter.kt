package com.example.fe.BookLetter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R


class BookAdapter(private val bookList: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookCover: ImageView = view.findViewById(R.id.book_cover)
        val bookTitle: TextView = view.findViewById(R.id.book_title)
        val bookAuthor: TextView = view.findViewById(R.id.book_author_publisher)  // ✅ ID 확인 후 수정
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_letter, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookCover.setImageResource(book.imageResId)  // ✅ 변수명 일치
        holder.bookTitle.text = book.title
        holder.bookAuthor.text = "${book.author} | ${book.publisher}"
    }

    override fun getItemCount(): Int = bookList.size
}
