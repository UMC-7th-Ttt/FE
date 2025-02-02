package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemMypageScrapFolderBinding
import com.example.fe.databinding.ItemMypageScrapFolderPlusBinding
import com.example.fe.mypage.MyPageScrap

class MyPageScrapRVAdapter(private val itemClickListener: MyItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val scraps = ArrayList<MyPageScrap>()

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_SPECIAL = 1
    }

    interface MyItemClickListener {
        fun onItemClick(myPageReview: MyPageScrap)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SPECIAL) {
            // 마지막 아이템에 대한 ViewHolder
            val binding: ItemMypageScrapFolderPlusBinding = ItemMypageScrapFolderPlusBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false)
            SpecialViewHolder(binding)
        } else {
            // 기본 아이템에 대한 ViewHolder
            val binding: ItemMypageScrapFolderBinding = ItemMypageScrapFolderBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false)
            ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val scrap = scraps[position]
        if (holder is ViewHolder) {
            holder.bind(scrap)
        } else if (holder is SpecialViewHolder) {
            holder.bind(scrap) // 특별한 아이템 바인딩
        }
    }

    override fun getItemCount(): Int = scraps.size

    override fun getItemViewType(position: Int): Int {
        return if (position == scraps.size - 1) { // 마지막 아이템일 경우
            TYPE_SPECIAL
        } else {
            TYPE_DEFAULT
        }
    }

    inner class ViewHolder(val binding: ItemMypageScrapFolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scrap: MyPageScrap) {
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(scrap)
            }
        }
    }

    inner class SpecialViewHolder(val binding: ItemMypageScrapFolderPlusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scrap: MyPageScrap) {
            // 특별 아이템 바인딩 로직
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setScrap(scrap: List<MyPageScrap>) {
        this.scraps.clear()  // 기존 데이터 제거
        this.scraps.addAll(scrap)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}