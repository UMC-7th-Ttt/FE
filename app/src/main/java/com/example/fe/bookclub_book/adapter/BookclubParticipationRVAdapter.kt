package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.bookclub_book.dataclass.BookclubParticipation
import com.example.fe.databinding.ItemBookclubBookParticipationBinding

class BookclubParticipationRVAdapter() : RecyclerView.Adapter<BookclubParticipationRVAdapter.ViewHolder>() {
    private val participations = ArrayList<BookclubParticipation>()

//    interface MyItemClickListener{
//        fun onRemoveSong(songId: Int)
//    }
//
//    private lateinit var mItemClickListener : MyItemClickListener
//
//    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
//        mItemClickListener = itemClickListener
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBookclubBookParticipationBinding = ItemBookclubBookParticipationBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(participations[position])
    }

    override fun getItemCount(): Int = participations.size


    inner class ViewHolder(val binding: ItemBookclubBookParticipationBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(participation: BookclubParticipation) {
            binding.itemParticipantBookTv.text = participation.book
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setParticipation(participation: List<BookclubParticipation>) {
        this.participations.clear()  // 기존 데이터 제거
        this.participations.addAll(participation)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}