package com.example.app.ui.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.BookLetter.LetterActivity
import com.example.fe.Home.Category.HomeBook
import com.example.fe.databinding.ItemHomeBookBinding

class HomeBookAdapter(private val bookList: List<HomeBook>) :
    RecyclerView.Adapter<HomeBookAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemHomeBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeBook) {
            // 🔹 Glide를 이용해 이미지 로딩 (URL 또는 리소스 ID 처리)
            Glide.with(binding.root.context)
                .load(item.imageUrl) // 여기에 URL을 넣으면 됨
                .placeholder(android.R.color.darker_gray) // 로딩 중 표시할 이미지
                .error(android.R.color.holo_red_dark) // 오류 발생 시 대체 이미지
                .into(binding.bookCover)

            Log.d("RecyclerView", "✅ bind() 호출됨 - 이미지 URL: ${item.imageUrl}")

            // ✅ 책 이미지 클릭 시 LetterActivity로 이동
            binding.bookCover.setOnClickListener {
                val intent = Intent(binding.root.context, LetterActivity::class.java)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemHomeBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        Log.d("RecyclerView", "✅ onBindViewHolder() 호출됨 - position: $position") // 🔹 Log 추가
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}
