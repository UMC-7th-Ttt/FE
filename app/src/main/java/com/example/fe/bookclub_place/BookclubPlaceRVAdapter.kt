package com.example.fe.bookclub_place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.R
import com.example.fe.databinding.ItemBookclubPlaceBinding
import com.example.fe.databinding.ItemBookclubPlaceFilterBinding

class BookclubPlaceRVAdapter(
    private val places: List<PlaceResponse>,
    private val onItemClick: (PlaceResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_FILTER = 0
        const val VIEW_TYPE_PLACE = 1
    }

    private var selectedFilterId: Int? = null

    inner class FilterViewHolder(val binding: ItemBookclubPlaceFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PlaceViewHolder(val binding: ItemBookclubPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceResponse) {
            binding.itemBookclubPlaceNameTv.text = place.title

            if(place.category == "BOOKSTORE") {
                binding.itemBookclubPlaceTagTv.text = "서점"
            }
            else {
                binding.itemBookclubPlaceTagTv.text = "카페"
            }
            binding.itemBookclubPlaceRatingTv.text = "${place.totalRating}"

            // 이미지 URL을 사용하여 이미지 로드 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 로딩 중일 때 보여줄 기본 이미지
                .into(binding.itemBookclubPlaceImg)

            // 북마크 상태에 따른 아이콘 변경
            binding.itemBookclubPlaceBookmarkIc.setImageResource(
                if (place.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )

            binding.itemBookclubPlaceImg.setOnClickListener {
                onItemClick(place)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_FILTER else VIEW_TYPE_PLACE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FILTER) {
            val binding = ItemBookclubPlaceFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FilterViewHolder(binding)
        } else {
            val binding = ItemBookclubPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PlaceViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilterViewHolder) {
            holder.binding.apply {
                placeFilterSelectedIv.setOnClickListener {
                    val filterBottomSheet = BookclubPlaceFilterBottomSheetFragment { selectedFilter ->
                        when (selectedFilter) {
                            "추천순" -> placeFilterSelectedIv.setBackgroundResource(R.drawable.btn_filter_recommendation)
                            "거리순" -> placeFilterSelectedIv.setBackgroundResource(R.drawable.btn_filter_distance)
                        }
                    }
                    filterBottomSheet.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "FilterBottomSheet")
                }
                placeFilterBookstoreIv.setOnClickListener { handleFilterClick(it.id, this) }
                placeFilterBookcafeIv.setOnClickListener { handleFilterClick(it.id, this) }
                updateFilterState(this)
            }
        } else if (holder is PlaceViewHolder) {
            holder.bind(places[position - 1])
        }
    }

    override fun getItemCount(): Int = places.size + 1

    private fun handleFilterClick(filterId: Int, binding: ItemBookclubPlaceFilterBinding) {
        selectedFilterId = if (selectedFilterId == filterId) null else filterId
        updateFilterState(binding)
    }

    private fun updateFilterState(binding: ItemBookclubPlaceFilterBinding) {
        binding.placeFilterBookstoreIv.background = if (selectedFilterId == binding.placeFilterBookstoreIv.id) {
            binding.root.context.getDrawable(R.drawable.btn_filter_bookstore_selected)
        } else {
            binding.root.context.getDrawable(R.drawable.btn_filter_bookstore)
        }
        binding.placeFilterBookcafeIv.background = if (selectedFilterId == binding.placeFilterBookcafeIv.id) {
            binding.root.context.getDrawable(R.drawable.btn_filter_bookcafe_selected)
        } else {
            binding.root.context.getDrawable(R.drawable.btn_filter_bookcafe)
        }
    }

}