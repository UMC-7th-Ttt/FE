package com.example.fe.BookLetter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

data class LetterBannerItem(val imageRes: Int, val title: String, val subtitle: String, val author: String)

class ViewPagerAdapter(private val banners: List<LetterBannerItem>) :
    RecyclerView.Adapter<ViewPagerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bannerImage: ImageView = view.findViewById(R.id.banner_image)
        val bannerTitle: TextView = view.findViewById(R.id.banner_title)
        val bannerSubtitle: TextView = view.findViewById(R.id.banner_subtitle)
        val bannerAuthor: TextView = view.findViewById(R.id.banner_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_letter_viewpager, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = banners[position]
        holder.bannerImage.setImageResource(banner.imageRes)
        holder.bannerTitle.text = banner.title
        holder.bannerSubtitle.text = banner.subtitle
        holder.bannerAuthor.text = banner.author
    }

    override fun getItemCount(): Int = banners.size
}
