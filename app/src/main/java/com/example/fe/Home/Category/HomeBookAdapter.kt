package com.example.fe.Home.Category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemBookImageBinding

class HomeBookAdapter(private val bookList: List<HomeBook>) :
    RecyclerView.Adapter<HomeBookAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemBookImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: HomeBook) {
            Glide.with(binding.root.context).load(book.imageUrl).into(binding.bookCover)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}
