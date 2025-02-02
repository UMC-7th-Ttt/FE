package com.example.fe.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

class ViewPagerAdapter(private val itemList: List<BannerItem>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.banner_image)
        val title: TextView = view.findViewById(R.id.banner_title)
        val subtitle: TextView = view.findViewById(R.id.banner_subtitle)
        val author: TextView = view.findViewById(R.id.banner_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_viewpager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.title.text = item.title
        holder.subtitle.text = item.subtitle
        holder.author.text = item.author
    }

    override fun getItemCount() = itemList.size
}
