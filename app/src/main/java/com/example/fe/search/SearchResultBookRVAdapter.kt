package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemSearchResultBookBinding
import com.example.fe.search.api.BookResponse

class SearchResultBookRVAdapter(private val bookList: List<BookResponse>) :
    RecyclerView.Adapter<SearchResultBookRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemSearchResultBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemSearchResultBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(book.cover)
                .placeholder(R.drawable.img_book_cover1)
                .into(itemSearchResultBookCoverIv)

            itemSearchResultBookTitleTv.text = book.title
            itemSearchResultBookAuthorTv.text = book.author
            itemSearchResultBookCategoryTv.text = book.category
            itemSearchResultBookPublisherTv.text = "출판사 ${book.publisher}"

            itemBookmarkIv.setImageResource(
                if (book.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }
    }

    override fun getItemCount(): Int = bookList.size
}
