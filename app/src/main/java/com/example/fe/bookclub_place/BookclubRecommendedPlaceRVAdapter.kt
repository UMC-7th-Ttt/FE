package com.example.fe.bookclub_place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.databinding.ItemBookclubRecommendedPlaceBinding

class BookclubRecommendedPlaceRVAdapter(
    private var places: List<PlaceResponse> = emptyList()
) : RecyclerView.Adapter<BookclubRecommendedPlaceRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBookclubRecommendedPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookclubRecommendedPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.binding.apply {
            itemBookclubRecommendedPlaceNameTv.text = place.title
            itemBookclubRecommendedPlaceTagTv.text = if (place.category == "CAFE") "카페" else "서점"
            itemBookclubRecommendedPlaceRatingTv.text = String.format("%.1f", place.totalRating)

            // 이미지 로드
            Glide.with(holder.itemView.context)
                .load(place.image)
                .placeholder(com.example.fe.R.drawable.img_place1)
                .into(itemBookclubRecommendedPlaceImg)
        }
    }

    override fun getItemCount(): Int = places.size

    // 데이터 업데이트 메서드
    fun updatePlaces(newPlaces: List<PlaceResponse>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}