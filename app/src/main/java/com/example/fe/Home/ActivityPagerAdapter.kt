package com.example.fe.Home


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.bookclub_book.BookclubBookDetail
import com.example.fe.databinding.ActivityCardBinding

class ActivityPagerAdapter(private val activityList: List<ActivityItem>) :
    RecyclerView.Adapter<ActivityPagerAdapter.ActivityViewHolder>() {

        var bookClubId: Int = 0

    inner class ActivityViewHolder(private val binding: ActivityCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ActivityItem) {
            binding.bookTitle.text = item.bookTitle
            binding.progressBar.progress = item.completionRate
            binding.progressPercentage.text = "${item.completionRate}% 완료"

            bookClubId = item.bookClubId

            binding.bookTitle.setOnClickListener {
                val intent = Intent(binding.root.context, BookclubBookDetail::class.java).apply {
                    putExtra("bookClubId", item.bookClubId)
                }
                binding.root.context.startActivity(intent)
            }

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
