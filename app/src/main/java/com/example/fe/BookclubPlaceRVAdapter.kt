package com.example.fe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemBookclubPlaceBinding
import com.example.fe.databinding.ItemBookclubPlaceFilterBinding

class BookclubPlaceRVAdapter(
    private val places: List<Place>,
    private val onItemClick: (Place) -> Unit // 클릭 이벤트 콜백 추가
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
        fun bind(place: Place) {
            binding.itemBookclubPlaceNameTv.text = place.name
            binding.itemBookclubPlaceTagTv.text = place.tag
            binding.itemBookclubPlaceRatingTv.text = "${place.rating}"
            binding.itemBookclubPlaceImg.setImageResource(place.imageResId)

            binding.itemBookclubPlaceBookmarkIc.setImageResource(
                if (place.isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
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
