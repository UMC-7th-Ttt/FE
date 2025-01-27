package com.example.fe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemBookclubPlaceRecentSearchBinding

class RecentSearchRVAdapter(
    private val recentSearches: List<String>
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
}
