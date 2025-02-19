package com.example.fe.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ActivityCardBinding

class ActivityPagerAdapter(private val activityList: List<ActivityItem>) :
    RecyclerView.Adapter<ActivityPagerAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(private val binding: ActivityCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ActivityItem) {
            binding.bookTitle.text = item.bookTitle
            binding.progressBar.progress = item.completionRate
            binding.progressPercentage.text = "${item.completionRate}% 완료"

            val context = binding.root.context
            Glide.with(context)
                .load(item.bookCover) // ✅ URL만 사용
                .into(binding.bookImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ActivityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(activityList[position])
    }

    override fun getItemCount(): Int = activityList.size
}
