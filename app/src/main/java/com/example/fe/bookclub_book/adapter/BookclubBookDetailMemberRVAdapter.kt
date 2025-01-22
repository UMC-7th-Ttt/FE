package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.bookclub_book.dataclass.BookclubDetailMember
import com.example.fe.bookclub_book.dataclass.BookclubMember
import com.example.fe.databinding.ItemBookclubBookDetailMemberBinding

class BookclubBookDetailMemberRVAdapter() : RecyclerView.Adapter<BookclubBookDetailMemberRVAdapter.ViewHolder>() {

    private val members = ArrayList<BookclubDetailMember>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookclubBookDetailMemberRVAdapter.ViewHolder {
        val binding: ItemBookclubBookDetailMemberBinding = ItemBookclubBookDetailMemberBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(members[position])
    }

    inner class ViewHolder(val binding: ItemBookclubBookDetailMemberBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(bookclubDetailMember: BookclubDetailMember) {
            binding.itemBookclubBookDetailMembersNameTv.text = bookclubDetailMember.name
            binding.itemBookclubBookDetailMembersImgIv.setImageResource(bookclubDetailMember.coverImg!!)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMembers(members: List<BookclubDetailMember>) {
        this.members.clear()  // 기존 데이터 제거
        this.members.addAll(members)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}