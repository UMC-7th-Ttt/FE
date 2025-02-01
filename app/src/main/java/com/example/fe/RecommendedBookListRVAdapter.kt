package com.example.fe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemRecommendedBookBinding

class RecommendedBookListRVAdapter(private val bookList: List<Book>) :
    RecyclerView.Adapter<RecommendedBookListRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemRecommendedBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemRecommendedBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.binding.apply {
            itemRecommendedBookIv.setImageResource(book.imageResId)
            itemRecommendedBookTitleTv.text = book.title
            itemRecommendedBookAuthorTv.text = book.author
            itemBookmarkIv.setImageResource(
                if (book.isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )

            // 북마크 버튼 클릭 이벤트 (토글)
            itemBookmarkIv.setOnClickListener {
                book.isBookmarked = !book.isBookmarked
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = bookList.size
}
