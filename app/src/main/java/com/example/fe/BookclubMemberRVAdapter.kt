package com.example.fe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemBookclubBookMembersBinding

class BookclubMemberRVAdapter() : RecyclerView.Adapter<BookclubMemberRVAdapter.ViewHolder>() {

    private val members = ArrayList<BookclubMember>()

//    interface MyItemClickListener{
//        fun onRemoveSong(songId: Int)
//    }
//
//    private lateinit var mItemClickListener : MyItemClickListener
//
//    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
//        mItemClickListener = itemClickListener
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookclubMemberRVAdapter.ViewHolder {
        val binding: ItemBookclubBookMembersBinding = ItemBookclubBookMembersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: BookclubMemberRVAdapter.ViewHolder, position: Int) {
        holder.bind(members[position])
    }

    inner class ViewHolder(val binding: ItemBookclubBookMembersBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(bookclubMember: BookclubMember) {
            binding.itemBookclubBookMemberNameTv.text = bookclubMember.name
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMembers(members: List<BookclubMember>) {
        this.members.clear()  // 기존 데이터 제거
        this.members.addAll(members)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}