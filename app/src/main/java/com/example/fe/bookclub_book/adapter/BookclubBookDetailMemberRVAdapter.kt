package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.bookclub_book.server.BookClubDetailResponse
import com.example.fe.databinding.ItemBookclubBookDetailMemberBinding

class BookclubBookDetailMemberRVAdapter : RecyclerView.Adapter<BookclubBookDetailMemberRVAdapter.ViewHolder>() {

    private val members = ArrayList<BookClubDetailResponse.Result.Member>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBookclubBookDetailMemberBinding = ItemBookclubBookDetailMemberBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(members[position])
    }

    inner class ViewHolder(val binding: ItemBookclubBookDetailMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookclubDetailMember: BookClubDetailResponse.Result.Member) {
            binding.itemBookclubBookDetailMembersNameTv.text = bookclubDetailMember.nickname
            Glide.with(binding.itemBookclubBookDetailMembersImgIv.context)
                .load(bookclubDetailMember.profileUrl)
                .into(binding.itemBookclubBookDetailMembersImgIv)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMembers(members: List<BookClubDetailResponse.Result.Member>) {
        this.members.clear()  // 기존 데이터 제거
        this.members.addAll(members)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}
