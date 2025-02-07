package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

class BookInfoTagRVAdapter(private var tags: List<String>) : RecyclerView.Adapter<BookInfoTagRVAdapter.TagViewHolder>() {

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tagTextView: TextView = itemView.findViewById(R.id.tag_tv)

        fun bind(tag: String) {
            tagTextView.text = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookclub_signup_book_info, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount() = tags.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTags(newTags: List<String>) {
        tags = newTags
        notifyDataSetChanged() // 데이터 변경을 알립니다.
    }
}
