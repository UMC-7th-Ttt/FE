package com.example.fe.mypage.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.fe.scrap.ScrapBottomSheetRVAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fe.databinding.ItemScrapMoveBinding

class MyPageScrapDetailBottomSheetRVAdapter(
    private val scrapList: List<Pair<String, Int>>
) : RecyclerView.Adapter<MyPageScrapDetailBottomSheetRVAdapter.ScrapViewHolder>() {

    inner class ScrapViewHolder(val binding: ItemScrapMoveBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val binding = ItemScrapMoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        val (title, imageResId) = scrapList[position]
        val binding = holder.binding

        binding.scrapItemBottomSheetTitleTv.text = title
        binding.scrapItemBottomSheetIv.setImageResource(imageResId)

        // 아이템 클릭 이벤트
        val onItemClicked = {
            notifyItemChanged(position)
        }

        binding.scrapItemBottomSheetIv.setOnClickListener { onItemClicked() }
        binding.scrapItemBottomSheetTitleTv.setOnClickListener { onItemClicked() }
    }

    override fun getItemCount(): Int = scrapList.size
}

