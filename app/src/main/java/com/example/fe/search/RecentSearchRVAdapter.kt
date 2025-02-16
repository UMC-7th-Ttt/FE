package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemBookclubPlaceRecentSearchBinding

class RecentSearchRVAdapter(
    private val recentSearches: MutableList<String>,
    private val onDeleteClick: (String) -> Unit // 삭제 이벤트 콜백 추가
) : RecyclerView.Adapter<RecentSearchRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBookclubPlaceRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookclubPlaceRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recentSearches[position]
        holder.binding.bookclubPlaceRecentSearchItemTv.text = item

        // 삭제 버튼 클릭 시 해당 검색어 삭제
        holder.binding.bookclubPlaceRecentSearchDeleteBtn.setOnClickListener {
            onDeleteClick(item) // 삭제 콜백 호출
        }
    }

    override fun getItemCount(): Int = recentSearches.size
}