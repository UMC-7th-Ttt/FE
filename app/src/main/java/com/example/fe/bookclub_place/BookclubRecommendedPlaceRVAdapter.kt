// BookclubRecommendedPlaceRVAdapter
package com.example.fe.bookclub_place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.Place
import com.example.fe.databinding.ItemBookclubRecommendedPlaceBinding

class BookclubRecommendedPlaceRVAdapter(
    private val places: List<Place>
) : RecyclerView.Adapter<BookclubRecommendedPlaceRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBookclubRecommendedPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookclubRecommendedPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.binding.apply {
            itemBookclubRecommendedPlaceNameTv.text = place.name
            itemBookclubRecommendedPlaceTagTv.text = place.tag
            itemBookclubRecommendedPlaceRatingTv.text = place.rating.toString()
            itemBookclubRecommendedPlaceImg.setImageResource(place.imageResId)
        }
    }

    override fun getItemCount(): Int = places.size
}
