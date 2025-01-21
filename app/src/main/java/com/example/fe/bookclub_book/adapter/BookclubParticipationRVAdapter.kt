package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.bookclub_book.dataclass.BookclubParticipation
import com.example.fe.databinding.ItemBookclubBookParticipationBinding

class BookclubParticipationRVAdapter(private val itemClickListener: MyItemClickListener) : RecyclerView.Adapter<BookclubParticipationRVAdapter.ViewHolder>() {
    private val participations = ArrayList<BookclubParticipation>()

    interface MyItemClickListener {
        fun onItemClick(participation: BookclubParticipation)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBookclubBookParticipationBinding = ItemBookclubBookParticipationBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(participations[position])
    }

    override fun getItemCount(): Int = participations.size

    inner class ViewHolder(val binding: ItemBookclubBookParticipationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(participation: BookclubParticipation) {
            binding.itemParticipantProgressBar.progress = participation.progress
            binding.itemParticipationProgressTv.text = "${participation.progress}%"

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(participation)
            }
        }
    }

    fun setParticipation(participation: List<BookclubParticipation>) {
        this.participations.clear()
        this.participations.addAll(participation)
        notifyDataSetChanged()
    }
}