package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemEditorPickPlaceBinding
import com.example.fe.bookclub_place.api.PlaceResponse

class EditorPickPlaceListRVAdapter(private val placeList: List<PlaceResponse>) :
    RecyclerView.Adapter<EditorPickPlaceListRVAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(private val binding: ItemEditorPickPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceResponse) {
            // 공간 이미지 설정 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 기본 이미지 설정
                .into(binding.itemEditorPickPlaceIv)

            // 공간 이름 설정
            binding.itemEditorPickPlaceTitleTv.text = place.title

            // 카테고리 변환 ("BOOKSTORE" -> "서점", "CAFE" -> "북카페")
            binding.itemEditorPickCategoryTv.text = when (place.category) {
                "BOOKSTORE" -> "서점"
                "CAFE" -> "북카페"
                else -> place.category
            }

            // 큐레이션 한 마디 설정 (카테고리에 따라 앞에 아이콘 추가)
            val curationPrefix = when (place.category) {
                "BOOKSTORE" -> "📙 "
                "CAFE" -> "☕️ "
                else -> ""
            }
            binding.itemCurationTitleTv.text = "$curationPrefix \"${place.curationTitle}\""

            // 북마크 아이콘 설정
            binding.itemBookmarkIv.setImageResource(
                if (place.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemEditorPickPlaceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(placeList[position])
    }

    override fun getItemCount(): Int = placeList.size
}
