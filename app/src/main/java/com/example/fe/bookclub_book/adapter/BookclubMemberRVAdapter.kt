package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.bookclub_book.server.ReadingRecordsListResponse
import com.example.fe.databinding.ItemBookclubBookMembersBinding


class BookclubMemberRVAdapter(private val itemClickListener: MyItemClickListener) : RecyclerView.Adapter<BookclubMemberRVAdapter.ViewHolder>() {

    private val members = ArrayList<ReadingRecordsListResponse.Result.ReadingRecord>()

    interface MyItemClickListener {
        fun onItemClick(readingRecordId: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBookclubBookMembersBinding = ItemBookclubBookMembersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(members[position])
    }

    inner class ViewHolder(val binding: ItemBookclubBookMembersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: ReadingRecordsListResponse.Result.ReadingRecord) {
            binding.itemBookclubBookMemberNameTv.text = member.memberNickName
            Glide.with(binding.itemBookclubBookMembersCoverImg.context)
                .load(member.imgUrl)
                .into(binding.itemBookclubBookMembersCoverImg)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(member.id)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMembers(members: List<ReadingRecordsListResponse.Result.ReadingRecord>) {
        this.members.clear()  // 기존 데이터 제거
        this.members.addAll(members)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}
