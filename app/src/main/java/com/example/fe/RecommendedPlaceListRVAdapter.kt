package com.example.fe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemRecommendedPlaceBinding

class RecommendedPlaceListRVAdapter(
    private val placeList: List<Place>
) : RecyclerView.Adapter<RecommendedPlaceListRVAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemRecommendedPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRecommendedPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]
        holder.binding.apply {
            itemRecommendedPlaceNameTv.text = place.name
            itemRecommendedPlaceTagTv.text = place.tag
            itemRecommendedPlaceRatingTv.text = place.rating.toString()
            itemRecommendedPlaceImg.setImageResource(place.imageResId)

            // 북마크 상태에 따라 아이콘 변경
            itemRecommendedPlaceBookmarkIv.setImageResource(
                if (place.isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )

            // 북마크 버튼 클릭 이벤트
            itemRecommendedPlaceBookmarkIv.setOnClickListener {
                place.isBookmarked = !place.isBookmarked
                notifyItemChanged(position) // 변경된 데이터 반영
            }
        }
    }

    override fun getItemCount(): Int = placeList.size
}
