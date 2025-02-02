package com.example.fe.BookLetter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.BookDetail.BookDetailActivity

class BookAdapter(private val bookList: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookCover: ImageView = view.findViewById(R.id.book_cover)
        val bookTitle: TextView = view.findViewById(R.id.book_title)
        val bookAuthor: TextView = view.findViewById(R.id.book_author_publisher)
        val arrowButton: ImageView = view.findViewById(R.id.btn_arrow)  // 🔹 변경
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
        holder.bookAuthor.text = "${book.author} | ${book.publisher}"

        // ⭐ btn_arrow 클릭 시 BookDetailActivity로 이동
        holder.arrowButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra("BOOK_TITLE", book.title) // 🔹 책 제목 전달 (옵션)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = bookList.size
}
