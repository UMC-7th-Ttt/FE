package com.example.fe.BookLetter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemLetterViewpagerBinding

class LetterAdapter(private val banners: List<LetterBannerItem>) :
    RecyclerView.Adapter<LetterAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(val binding: ItemLetterViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemLetterViewpagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = banners[position]
        holder.binding.bannerTitle.text = banner.title
        holder.binding.bannerSubtitle.text = banner.subtitle
        holder.binding.bannerAuthor.text = banner.author

        // ✅ Glide를 사용하여 이미지 로드
        Glide.with(holder.itemView.context)
            .load(banner.imageRes)  // imageRes는 URL (String)
            .into(holder.binding.bannerImage)
    }

    override fun getItemCount(): Int = banners.size
}
