package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemMypageScrapDetailBinding
import com.example.fe.databinding.ItemMypageScrapFolderBinding
import com.example.fe.databinding.ItemMypageScrapFolderPlusBinding
import com.example.fe.mypage.MyPageReview
import com.example.fe.mypage.MyPageScrap
import com.example.fe.mypage.MyPageScrapFolder

class MyPageScrapRVAdapter(private val itemClickListener: MyItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val myPageScrapFolders = ArrayList<MyPageScrapFolder>()
    private var isEditMode = false
    private val selectedItems = mutableSetOf<MyPageScrapFolder>()


    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_SPECIAL = 1
    }

    interface MyItemClickListener {
        fun onItemClick(myPageScrapFolder: MyPageScrapFolder)
        fun onSelectionChanged(selectedCount: Int)
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
        val scrapFolders = myPageScrapFolders[position]
        if (holder is ViewHolder) {
            holder.bind(scrapFolders)
        } else if (holder is SpecialViewHolder) {
            holder.bind(scrapFolders) // 특별한 아이템 바인딩
        }
    }

    override fun getItemCount(): Int = myPageScrapFolders.size

    override fun getItemViewType(position: Int): Int {
        return if (position == myPageScrapFolders.size - 1) { // 마지막 아이템일 경우
            TYPE_SPECIAL
        } else {
            TYPE_DEFAULT
        }
    }

    inner class ViewHolder(val binding: ItemMypageScrapFolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myPageScrapFolder: MyPageScrapFolder) {
            binding.root.setOnClickListener {
                if (isEditMode) {
                    toggleItemSelection(myPageScrapFolder)
                } else {
                    itemClickListener.onItemClick(myPageScrapFolder)
                }
            }

            // 체크 표시 설정
            binding.checkOver.visibility = if (selectedItems.contains(myPageScrapFolder)) {
                View.VISIBLE // 선택된 경우 체크 표시 보이기
            } else {
                View.GONE // 선택되지 않은 경우 체크 표시 숨기기
            }
        }
    }


    inner class SpecialViewHolder(val binding: ItemMypageScrapFolderPlusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scrapFolder: MyPageScrapFolder) {
            // 특별 아이템 바인딩 로직
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setScrap(scrapFolder: List<MyPageScrapFolder>) {
        this.myPageScrapFolders.clear()  // 기존 데이터 제거
        this.myPageScrapFolders.addAll(scrapFolder)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyDataSetChanged() // 뷰 업데이트
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun toggleItemSelection(myPageScrapFolder: MyPageScrapFolder) {
        if (selectedItems.contains(myPageScrapFolder)) {
            selectedItems.remove(myPageScrapFolder)
        } else {
            selectedItems.add(myPageScrapFolder)
        }
        itemClickListener.onSelectionChanged(selectedItems.size) // 선택 상태 변경 알림
        notifyDataSetChanged() // 데이터 변경 알림
    }

    fun getSelectedItems(): List<MyPageScrapFolder> {
        return selectedItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        selectedItems.clear() // 선택된 아이템 목록 비우기
        notifyDataSetChanged() // 데이터 변경 알림
    }
}