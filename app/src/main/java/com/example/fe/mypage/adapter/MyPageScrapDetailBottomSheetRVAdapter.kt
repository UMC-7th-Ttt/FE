package com.example.fe.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemScrapBottomSheetBinding
import com.example.fe.mypage.ScrapFolderResponse

class MyPageScrapDetailBottomSheetRVAdapter(
    private var folders: List<ScrapFolderResponse.Result.Folder>,
    private val onFolderSelected: (Long) -> Unit
) : RecyclerView.Adapter<MyPageScrapDetailBottomSheetRVAdapter.ScrapViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ScrapViewHolder(val binding: ItemScrapBottomSheetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: ScrapFolderResponse.Result.Folder) {
            binding.scrapItemBottomSheetTitleTv.text = folder.name

            // 이미지가 있는 경우에만 Glide를 사용해 로드
            if (folder.images.isNotEmpty()) {
                Glide.with(binding.scrapItemBottomSheetIv.context)
                    .load(folder.images.first())
                    .into(binding.scrapItemBottomSheetIv)
            } else {
                binding.scrapItemBottomSheetIv.setImageResource(android.R.color.transparent)
            }

            // 라디오 버튼 설정
            binding.scrapBottomSheetSelect.isChecked = (adapterPosition == selectedPosition)
            binding.scrapBottomSheetSelect.buttonTintList = ContextCompat.getColorStateList(
                binding.scrapBottomSheetSelect.context,
                if (adapterPosition == selectedPosition) R.color.colorPrimaryNormal else R.color.colorLabelNeutral
            )
            binding.scrapBottomSheetSelect.setOnClickListener {
                if (selectedPosition != adapterPosition) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                    onFolderSelected(folder.folderId.toLong())
                } else {
                    binding.scrapBottomSheetSelect.isChecked = false
                    selectedPosition = RecyclerView.NO_POSITION
                    notifyItemChanged(adapterPosition)
                }
            }

            // 아이템 클릭 이벤트
            binding.root.setOnClickListener {
                if (selectedPosition != adapterPosition) {
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    onFolderSelected(folder.folderId.toLong())
                } else {
                    selectedPosition = RecyclerView.NO_POSITION
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val binding = ItemScrapBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        holder.bind(folders[position])
    }

    override fun getItemCount(): Int = folders.size

    fun updateFolders(newFolders: List<ScrapFolderResponse.Result.Folder>) {
        folders = newFolders
        notifyDataSetChanged()
    }
}