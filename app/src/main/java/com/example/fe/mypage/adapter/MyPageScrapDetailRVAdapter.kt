package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemMypageScrapDetailBinding
import com.example.fe.mypage.MyPageScrap

class MyPageScrapDetailRVAdapter(private val itemClickListener: MyItemClickListener) :
    RecyclerView.Adapter<MyPageScrapDetailRVAdapter.ViewHolder>() {

    private val mypageScraps = ArrayList<MyPageScrap>()
    private var isEditMode = false // 편집 모드 상태
    private val selectedItems = mutableSetOf<MyPageScrap>() // 선택된 아이템 목록

    interface MyItemClickListener {
        fun onItemClick(myPageScrap: MyPageScrap)
        fun onSelectionChanged(selectedCount: Int) // 선택 상태 변경 알림
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMypageScrapDetailBinding = ItemMypageScrapDetailBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mypageScraps[position])
    }

    override fun getItemCount(): Int = mypageScraps.size

    inner class ViewHolder(val binding: ItemMypageScrapDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myPageScrap: MyPageScrap) {
            binding.root.setOnClickListener {
                if (isEditMode) {
                    toggleItemSelection(myPageScrap)
                } else {
                    itemClickListener.onItemClick(myPageScrap)
                }
            }

            // 체크 표시 설정
            binding.checkOver.visibility = if (selectedItems.contains(myPageScrap)) {
                View.VISIBLE // 선택된 경우 체크 표시 보이기
            } else {
                View.GONE // 선택되지 않은 경우 체크 표시 숨기기
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setScrap(myPageScraps: List<MyPageScrap>) {
        this.mypageScraps.clear()  // 기존 데이터 제거
        this.mypageScraps.addAll(myPageScraps)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyDataSetChanged() // 뷰 업데이트
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun toggleItemSelection(myPageScrap: MyPageScrap) {
        if (selectedItems.contains(myPageScrap)) {
            selectedItems.remove(myPageScrap)
        } else {
            selectedItems.add(myPageScrap)
        }
        itemClickListener.onSelectionChanged(selectedItems.size) // 선택 상태 변경 알림
        notifyDataSetChanged() // 데이터 변경 알림
    }

    fun getSelectedItems(): List<MyPageScrap> {
        return selectedItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        selectedItems.clear() // 선택된 아이템 목록 비우기
        notifyDataSetChanged() // 데이터 변경 알림
    }
}
