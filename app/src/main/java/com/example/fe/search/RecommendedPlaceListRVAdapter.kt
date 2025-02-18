package com.example.fe.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.databinding.ItemRecommendedPlaceBinding

class RecommendedPlaceListRVAdapter(
    private val placeList: List<PlaceResponse>
) : RecyclerView.Adapter<RecommendedPlaceListRVAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemRecommendedPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceResponse) {
            binding.itemRecommendedPlaceNameTv.text = place.title
            binding.itemRecommendedPlaceRatingTv.text = String.format("%.1f", place.totalRating)

            // 이미지 설정 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 기본 이미지 설정
                .into(binding.itemRecommendedPlaceImg)

            // 카테고리 변환 ("BOOKSTORE" -> "서점", "CAFE" -> "북카페")
            binding.itemRecommendedPlaceTagTv.text = when (place.category) {
                "BOOKSTORE" -> "서점"
                "CAFE" -> "북카페"
                else -> place.category
            }

            // 북마크 아이콘 설정
            binding.itemRecommendedPlaceBookmarkIv.setImageResource(
                if (place.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRecommendedPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(placeList[position])
    }

    override fun getItemCount(): Int = placeList.size
}