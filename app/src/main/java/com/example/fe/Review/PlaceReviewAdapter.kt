package com.example.fe.Review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemReviewPlaceBinding

class PlaceReviewAdapter(private val placeList: MutableList<PlaceReviewItem>) :
    RecyclerView.Adapter<PlaceReviewAdapter.PlaceReviewViewHolder>() {

    inner class PlaceReviewViewHolder(private val binding: ItemReviewPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceReviewItem) {
            binding.placeName.text = place.placeTitle
            binding.placeLocation.text = place.location
            binding.placeRatingBar.rating = place.rating
            binding.placeRatingText.text = String.format("%.1f", place.rating)
            Glide.with(binding.root.context)
                .load(place.imageUrl)
                .into(binding.placeImage)  // 장소 이미지 로드
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceReviewViewHolder {
        val binding = ItemReviewPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceReviewViewHolder, position: Int) {
        holder.bind(placeList[position])
    }

    override fun getItemCount(): Int = placeList.size

    // 📌 새 장소 추가 메서드
    fun addPlace(place: PlaceReviewItem) {
        placeList.add(place)
        notifyItemInserted(placeList.size - 1)
    }
}
