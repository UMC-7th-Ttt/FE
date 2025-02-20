package com.example.fe.Home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemReminderSectionBinding

class ReminderSectionRVAdapter : RecyclerView.Adapter<ReminderSectionRVAdapter.ViewHolder>() {

    private val reminderReviewList = ArrayList<RemindReviewItem>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemReminderSectionBinding = ItemReminderSectionBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reminderReviewList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reminderReviewList[position])
    }

    inner class ViewHolder(val binding: ItemReminderSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(remindReviewItem: RemindReviewItem) {
            binding.card1Title.text = remindReviewItem.bookTitle
            binding.card1Description.text = remindReviewItem.content
            binding.card1Date.text = remindReviewItem.writeDate.split("-")[2]
            binding.card1Month.text = when (remindReviewItem.writeDate.split("-")[1].toInt()) {
                1 -> "JAN"
                2 -> "FEB"
                3 -> "MAR"
                4 -> "APR"
                5 -> "MAY"
                6 -> "JUN"
                7 -> "JUL"
                8 -> "AUG"
                9 -> "SEP"
                10 -> "OCT"
                11 -> "NOV"
                12 -> "DEC"
                else -> "Invalid Month"
            }

            // Safe call을 사용하여 Glide 호출
            binding.root.context?.let {
                Glide.with(it)
                    .load(remindReviewItem.bookCover)
                    .into(binding.card1Image)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setReminders(reminders: List<RemindReviewItem>) {
        this.reminderReviewList.clear()  // 기존 데이터 제거
        this.reminderReviewList.addAll(reminders)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}