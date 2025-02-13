// PopularBookListRVAdapter
package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemPopularBookBinding
import com.example.fe.search.api.BookResponse

class PopularBookListRVAdapter(private val bookList: List<BookResponse>) :
    RecyclerView.Adapter<PopularBookListRVAdapter.PopularBookViewHolder>() {

    inner class PopularBookViewHolder(private val binding: ItemPopularBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookResponse) {
            Glide.with(binding.root.context)
                .load(book.cover)
                .placeholder(R.drawable.img_book_cover1)
                .into(binding.itemPopularBookIv)

            binding.itemBookmarkIv.setImageResource(
                if (book.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBookViewHolder {
        val binding = ItemPopularBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularBookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularBookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}