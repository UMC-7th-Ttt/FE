package com.example.fe.Review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemReviewBookBinding

class ReviewBookAdapter(private val bookList: MutableList<ReviewItem>) :
    RecyclerView.Adapter<ReviewBookAdapter.ReviewBookViewHolder>() {

    inner class ReviewBookViewHolder(private val binding: ItemReviewBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: ReviewItem) {
            binding.bookTitle.text = book.bookTitle
            binding.bookAuthor.text = book.author
            Glide.with(binding.root.context)
                .load(book.coverUrl)
                .into(binding.bookCover)  // 책 표지 이미지 로드
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewBookViewHolder {
        val binding = ItemReviewBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewBookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewBookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    // 📌 새 아이템 추가 메서드
    fun addBook(book: ReviewItem) {
        bookList.add(book)
        notifyItemInserted(bookList.size - 1)
    }
}
