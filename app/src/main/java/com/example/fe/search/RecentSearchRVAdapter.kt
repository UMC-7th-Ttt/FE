package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemBookclubPlaceRecentSearchBinding

class RecentSearchRVAdapter(
    private val recentSearches: MutableList<String>
) : RecyclerView.Adapter<RecentSearchRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBookclubPlaceRecentSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookclubPlaceRecentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recentSearches[position]
        holder.binding.bookclubPlaceRecentSearchItemTv.text = item
    }

    override fun getItemCount(): Int = recentSearches.size

    // 리스트 초기화 및 RecyclerView 갱신 메서드 추가
    fun clearData() {
        recentSearches.clear() // 데이터 삭제
        notifyDataSetChanged() // RecyclerView 업데이트
    }
}
