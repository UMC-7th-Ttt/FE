package com.example.fe.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.BookDetail.BookDetailActivity
import com.example.fe.R
import com.example.fe.Review.BookReviewActivity
import com.example.fe.databinding.ItemSearchResultBookBinding
import com.example.fe.search.api.BookResponse

class SearchResultBookRVAdapter(
    private val bookList: List<BookResponse>,
    private val callerActivity: String? // 호출한 액티비티 정보 추가
) :
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

            // 아이템 클릭 시 BookDetailActivity로 이동 (책 ID만 전달)
//            root.setOnClickListener {
//                val context = root.context
//                val intent = Intent(context, BookDetailActivity::class.java).apply {
//                    putExtra("BOOK_ID", book.id) // 책 ID만 전달
//                }
//                context.startActivity(intent)
//            }

            // 아이템 클릭 시 이동하는 액티비티 결정
//            root.setOnClickListener {
//                val context = root.context
//                val intent = if (callerActivity == "ReviewActivity") {
//                    Intent(context, BookReviewActivity::class.java)
//                } else {
//                    Intent(context, BookDetailActivity::class.java)
//                }.apply {
//                    putExtra("BOOK_ID", book.id) // 책 ID 전달
//                }
//                context.startActivity(intent)
//            }

            // 아이템 클릭 시 이동하는 액티비티 결정
            root.setOnClickListener {
                val context = root.context
                val intent = if (callerActivity == "ReviewActivity") {
                    Intent(context, BookReviewActivity::class.java)
                } else {
                    Intent(context, BookDetailActivity::class.java)
                }.apply {
                    putExtra("BOOK_ID", book.id)           // 책 ID 전달
                    putExtra("BOOK_TITLE", book.title)     // 책 제목 전달
                    putExtra("BOOK_COVER", book.cover)     // 책 이미지 URL 전달
                }
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int = bookList.size
}
