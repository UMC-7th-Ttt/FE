package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.Book
import com.example.fe.R
import com.example.fe.databinding.ItemEditorPickBookBinding

class EditorPickBookListRVAdapter(private val bookList: List<Book>) :
    RecyclerView.Adapter<EditorPickBookListRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemEditorPickBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemEditorPickBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.binding.apply {
            itemBookCoverIv.setImageResource(book.imageResId)
            itemBookTitleTv.text = book.title
            itemBookAuthorTv.text = book.author
            itemBookLetterTitleTv.text = book.letterTitle
//            itemBookLetterTitleTv.text = "\"${book.letterTitle}\"" // 큰따옴표 포함해서 출력

            // 북마크 상태에 따라 아이콘 변경
            itemBookmarkIv.setImageResource(
                if (book.isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )

            // 북마크 버튼 클릭 시 상태 변경
            itemBookmarkIv.setOnClickListener {
                book.isBookmarked = !book.isBookmarked
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = bookList.size
}
