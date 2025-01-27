package com.example.fe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemScrapBinding

class ScrapBottomSheetRVAdapter(private val scrapList: List<Pair<String, Int>>) :
    RecyclerView.Adapter<ScrapBottomSheetRVAdapter.ScrapViewHolder>() {

    inner class ScrapViewHolder(val binding: ItemScrapBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val binding = ItemScrapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        val (title, imageResId) = scrapList[position]
        holder.binding.scrapItemBottomSheetTitleTv.text = title
        holder.binding.scrapItemBottomSheetIv.setImageResource(imageResId)
    }

    override fun getItemCount(): Int = scrapList.size
}
