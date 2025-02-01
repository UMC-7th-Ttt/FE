package com.example.fe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemPopularBookBinding

class PopularBookListRVAdapter(private val bookList: List<Pair<Int, Boolean>>) :
    RecyclerView.Adapter<PopularBookListRVAdapter.PopularBookViewHolder>() {

    // 내부적으로 북마크 상태 관리
    private val mutableBookList = bookList.toMutableList()

    inner class PopularBookViewHolder(private val binding: ItemPopularBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val (imageResId, isBookmarked) = mutableBookList[position]

            binding.itemPopularBookIv.setImageResource(imageResId)
            binding.itemBookmarkIv.setImageResource(
                if (isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )

            // 북마크 클릭 이벤트 (토글)
            binding.itemBookmarkIv.setOnClickListener {
                mutableBookList[position] = Pair(imageResId, !isBookmarked)
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBookViewHolder {
        val binding =
            ItemPopularBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularBookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularBookViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = mutableBookList.size
}
