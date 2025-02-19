package com.example.fe.Home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.BookLetter.LetterActivity
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

            binding.root.setOnClickListener {
                val context = binding.root.context

                Log.d("ViewPagerAdapter", "📡 클릭된 bookLetterId: ${item.bookLetterId}") // ✅ 로그 확인

                val intent = Intent(context, LetterActivity::class.java)
                intent.putExtra("bookLetterId", item.bookLetterId.toLong()) // ✅ bookLetterId 전달
                context.startActivity(intent)

                Log.d("ViewPagerAdapter", "📡 Intent 실행 완료 (bookLetterId: ${item.bookLetterId})")
            }
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
