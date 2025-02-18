package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemEditorPickBookBinding
import com.example.fe.search.api.BookResponse

class EditorPickBookListRVAdapter(
    private val bookList: List<BookResponse>,
    private val bookLetterTitle: String // 북레터 제목 추가
) : RecyclerView.Adapter<EditorPickBookListRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemEditorPickBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookResponse) {
            // 책 커버 이미지 설정 (Glide 사용)
            Glide.with(binding.root.context)
                .load(book.cover)
                .placeholder(R.drawable.img_book_cover7) // 기본 이미지 설정
                .into(binding.itemBookCoverIv)

            // 책 제목, 저자 및 북레터 제목 설정
            binding.itemBookTitleTv.text = book.title
            binding.itemBookAuthorTv.text = book.author
            binding.itemBookLetterTitleTv.text = "📙 \"${bookLetterTitle}\"" // 북레터 제목 적용

            // 북마크 아이콘 설정
            binding.itemBookmarkIv.setImageResource(
                if (book.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemEditorPickBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}