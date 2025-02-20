package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fe.bookclub_book.server.BookClubParticipationResponse
import com.example.fe.databinding.ItemBookclubBookParticipationBinding

class BookclubParticipationRVAdapter(private val itemClickListener: MyItemClickListener) : RecyclerView.Adapter<BookclubParticipationRVAdapter.ViewHolder>() {
    private val participations = ArrayList<BookClubParticipationResponse.Result.BookClub>()

    interface MyItemClickListener {
        fun onItemClick(participation: BookClubParticipationResponse.Result.BookClub)
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
        fun bind(participation: BookClubParticipationResponse.Result.BookClub) {
            binding.itemParticipantProgressBar.progress = participation.completionRate
            binding.itemParticipationProgressTv.text = "${participation.completionRate}%"
            binding.titleTv.text = participation.bookTitle
            binding.authorTv.text = participation.bookAuthor

            Glide.with(binding.bookBgIv.context)
                .load(participation.bookCover)
                .into(binding.bookBgIv)

            Glide.with(binding.bookIv.context)
                .load(participation.bookCover)
                .into(binding.bookIv)

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(participation)
            }

            binding.titleTv.apply {
                isSelected = true
            }

            binding.authorTv.apply {
                isSelected = true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setParticipation(participation: List<BookClubParticipationResponse.Result.BookClub>) {
        this.participations.clear()
        this.participations.addAll(participation)
        notifyDataSetChanged()
    }
}