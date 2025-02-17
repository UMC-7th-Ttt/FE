package com.example.fe.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemHomeViewpagerBinding

class ViewPagerAdapter(private val bannerList: List<BannerItem>) :
    RecyclerView.Adapter<ViewPagerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(private val binding: ItemHomeViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BannerItem) {
            // ✅ Glide를 사용하여 URL 이미지 로드
            Glide.with(binding.root.context)
                .load(item.coverImg) // 🔹 API에서 받은 URL 이미지
                .into(binding.bannerImage)

            binding.bannerTitle.text = item.title
            binding.bannerSubtitle.text = item.subtitle
            binding.bannerAuthor.text = item.author
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemHomeViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int = bannerList.size
}
