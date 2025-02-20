package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemMypageScrapFolderBinding
import com.example.fe.databinding.ItemMypageScrapFolderPlusBinding
import com.example.fe.mypage.MyPageScrapDetail
import com.example.fe.mypage.server.ScrapFolderResponse
import com.example.fe.mypage.ScrapNewFolder

class MyPageScrapRVAdapter(private val itemClickListener: MyItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val folders = ArrayList<ScrapFolderResponse.Result.Folder>()
    private var isEditMode = false
    private val selectedItems = mutableSetOf<ScrapFolderResponse.Result.Folder>()
    private val nonDeletableFolderIds = setOf(16, 17) // 기본 폴더 ID

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_SPECIAL = 1
    }

    interface MyItemClickListener {
        fun onItemClick(folderId: Int)
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
        if (holder is ViewHolder) {
            holder.bind(folders[position])
        } else if (holder is SpecialViewHolder) {
            holder.bind() // 특별한 아이템 바인딩
        }
    }

    override fun getItemCount(): Int = folders.size + 1 // 새 폴더 아이템 추가

    override fun getItemViewType(position: Int): Int {
        return if (position == folders.size) { // 마지막 아이템일 경우
            TYPE_SPECIAL
        } else {
            TYPE_DEFAULT
        }
    }

    inner class ViewHolder(val binding: ItemMypageScrapFolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: ScrapFolderResponse.Result.Folder) {
            binding.root.setOnClickListener {
                if (isEditMode) {
                    toggleItemSelection(folder)
                } else {
                    val context = binding.root.context
                    val intent = Intent(context, MyPageScrapDetail::class.java)
                    intent.putExtra("folderId", folder.folderId)
                    intent.putExtra("folderName", folder.name)
                    context.startActivity(intent)
                }
            }

            // 체크 표시 설정
            binding.checkOver.visibility = if (selectedItems.contains(folder)) {
                View.VISIBLE // 선택된 경우 체크 표시 보이기
            } else {
                View.GONE // 선택되지 않은 경우 체크 표시 숨기기
            }

            // 기본 폴더는 선택 불가
            if(isEditMode) {
                binding.root.isEnabled = folder.folderId !in nonDeletableFolderIds
            }


            binding.folderNameTv.text = folder.name

            // 이미지뷰 목록
            val imageViews = listOf(binding.scrapFolderIv1, binding.scrapFolderIv2, binding.scrapFolderIv3, binding.scrapFolderIv4)

            // 이미지뷰 초기화
            imageViews.forEach { it.setImageResource(android.R.color.transparent) }

            // 이미지가 있는 경우에만 Glide를 사용해 로드
            folder.images.forEachIndexed { index, imageUrl ->
                if (index < imageViews.size) {
                    Glide.with(imageViews[index].context)
                        .load(imageUrl)
                        .placeholder(android.R.color.transparent) // 플레이스홀더 추가
                        .error(android.R.color.transparent) // 에러 이미지 추가
                        .into(imageViews[index])
                }
            }
        }
    }

    inner class SpecialViewHolder(val binding: ItemMypageScrapFolderPlusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // 특별 아이템 바인딩 로직
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ScrapNewFolder::class.java)
                context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setScrap(folders: List<ScrapFolderResponse.Result.Folder>) {
        this.folders.clear()  // 기존 데이터 제거
        this.folders.addAll(folders)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyDataSetChanged() // 뷰 업데이트
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun toggleItemSelection(folder: ScrapFolderResponse.Result.Folder) {
        if (selectedItems.contains(folder)) {
            selectedItems.remove(folder)
        } else {
            selectedItems.add(folder)
        }
        itemClickListener.onSelectionChanged(selectedItems.size) // 선택 상태 변경 알림
        notifyDataSetChanged() // 데이터 변경 알림
    }

    fun getSelectedItems(): List<ScrapFolderResponse.Result.Folder> {
        return selectedItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        selectedItems.clear() // 선택된 아이템 목록 비우기
        notifyDataSetChanged() // 데이터 변경 알림
    }
}