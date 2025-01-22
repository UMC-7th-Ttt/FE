package com.example.fe.mypage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemMypageScrapBinding
import com.example.fe.databinding.ItemMypageScrapPlusBinding

class MyPageScrapRVAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val scraps = ArrayList<MyPageScrap>()

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_SPECIAL = 1
    }

//    interface MyItemClickListener{
//        fun onRemoveSong(songId: Int)
//    }
//
//    private lateinit var mItemClickListener : MyItemClickListener
//
//    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
//        mItemClickListener = itemClickListener
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SPECIAL) {
            // 마지막 아이템에 대한 ViewHolder
            val binding: ItemMypageScrapPlusBinding = ItemMypageScrapPlusBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false)
            SpecialViewHolder(binding)
        } else {
            // 기본 아이템에 대한 ViewHolder
            val binding: ItemMypageScrapBinding = ItemMypageScrapBinding.inflate(
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

    inner class ViewHolder(val binding: ItemMypageScrapBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scrap: MyPageScrap) {
            // 기본 아이템 바인딩 로직
        }
    }

    inner class SpecialViewHolder(val binding: ItemMypageScrapPlusBinding) : RecyclerView.ViewHolder(binding.root) {
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