package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemRecommendedBookBinding
import com.example.fe.search.api.BookResponse

class RecommendedBookListRVAdapter(private val bookList: List<BookResponse>) :
    RecyclerView.Adapter<RecommendedBookListRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemRecommendedBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookResponse) {
            // 커버 이미지 설정 (Glide 사용)
            Glide.with(binding.root.context)
                .load(book.cover)
                .placeholder(R.drawable.img_book_cover7) // 기본 이미지 설정
                .into(binding.itemRecommendedBookIv)

            // 도서 제목 및 저자 설정
            binding.itemRecommendedBookTitleTv.text = book.title
            binding.itemRecommendedBookAuthorTv.text = book.author

            // 북마크 아이콘 설정
            binding.itemBookmarkIv.setImageResource(
                if (book.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemRecommendedBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}