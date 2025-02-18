package com.example.fe.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemSearchResultPlaceBinding
import com.example.fe.bookclub_place.api.PlaceResponse

class SearchResultPlaceRVAdapter(
    private val places: List<PlaceResponse>,
    private val onItemClick: (PlaceResponse) -> Unit
) : RecyclerView.Adapter<SearchResultPlaceRVAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSearchResultPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceResponse) {
            binding.itemSearchResultPlaceNameTv.text = place.title
            binding.itemSearchResultPlaceRatingTv.text = String.format("%.1f", place.totalRating)

            // 이미지 로딩 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 기본 이미지
                .into(binding.itemSearchResultPlaceIv)

            // 장소 태그
            if (place.category == "BOOKSTORE") {
                binding.itemSearchResultPlaceTagTv.text = "서점"
            } else {
                binding.itemSearchResultPlaceTagTv.text = "카페"
            }

            // 북마크 상태에 따른 아이콘 변경
            binding.itemSearchResultPlaceBookmarkIc.setImageResource(
                if (place.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )

            binding.itemSearchResultPlaceIv.setOnClickListener {
                onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int = places.size
}
