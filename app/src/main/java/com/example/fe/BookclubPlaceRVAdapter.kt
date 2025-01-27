package com.example.fe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.Place
import com.example.fe.databinding.ItemBookclubPlaceBinding
import com.example.fe.BookclubPlaceDetailFragment
import com.example.fe.databinding.ItemBookclubPlaceFilterBinding

class BookclubPlaceRVAdapter(private val places: List<Place>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_FILTER = 0
        const val VIEW_TYPE_PLACE = 1
    }

    // 선택된 필터의 ID 저장 (recommendation 제외)
    private var selectedFilterId: Int? = null

    inner class FilterViewHolder(val binding: ItemBookclubPlaceFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PlaceViewHolder(val binding: ItemBookclubPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_FILTER else VIEW_TYPE_PLACE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FILTER) {
            val binding = ItemBookclubPlaceFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            FilterViewHolder(binding)
        } else {
            val binding = ItemBookclubPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            PlaceViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilterViewHolder) {
            holder.binding.apply {
                // 필터 버튼 클릭 시 바텀시트 표시
                placeFilterSelectedIv.setOnClickListener {
                    val filterBottomSheet = BookclubPlaceFilterBottomSheetFragment { selectedFilter ->
                        when (selectedFilter) {
                            "추천순" -> {
                                placeFilterSelectedIv.setBackgroundResource(R.drawable.btn_filter_recommendation)
                            }
                            "거리순" -> {
                                placeFilterSelectedIv.setBackgroundResource(R.drawable.btn_filter_distance)
                            }
                        }
                    }
                    (holder.itemView.context as AppCompatActivity).supportFragmentManager
                        .beginTransaction()
                        .add(filterBottomSheet, "FilterBottomSheet")
                        .commitAllowingStateLoss()
                }

                // bookstore 필터 클릭
                placeFilterBookstoreIv.setOnClickListener {
                    handleFilterClick(it.id, this)
                }

                // bookcafe 필터 클릭
                placeFilterBookcafeIv.setOnClickListener {
                    handleFilterClick(it.id, this)
                }

                // 필터 상태 갱신
                updateFilterState(this)
            }
        } else if (holder is PlaceViewHolder) {
            val place = places[position - 1]
            holder.binding.apply {
                itemBookclubPlaceNameTv.text = place.name
                itemBookclubPlaceTagTv.text = place.tag
                itemBookclubPlaceRatingTv.text = "${place.rating}"
                itemBookclubPlaceImg.setImageResource(place.imageResId)

                // 북마크 아이콘 설정
                itemBookclubPlaceBookmarkIc.setImageResource(
                    if (place.isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
                )

                // 북마크 버튼 클릭 리스너
//                itemBookclubPlaceBookmarkIc.setOnClickListener {
//                    place.isBookmarked = !place.isBookmarked // 상태 토글
//                    notifyItemChanged(position) // 뷰 갱신
//                }

                itemBookclubPlaceImg.setOnClickListener {
                    // 디테일 프래그먼트로 이동
                    (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.bookclub_place_frm, BookclubPlaceDetailFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return places.size + 1 // 필터 버튼 + 장소 리스트
    }

    // 필터 클릭 핸들러
    private fun handleFilterClick(filterId: Int, binding: ItemBookclubPlaceFilterBinding) {
        // recommendation 필터는 상태 관리 제외
        if (selectedFilterId == filterId) {
            // 동일 필터를 다시 클릭하면 선택 해제
            selectedFilterId = null
        } else {
            // 새 필터 선택
            selectedFilterId = filterId
        }
        updateFilterState(binding)
    }

    // 필터 상태 업데이트
    private fun updateFilterState(binding: ItemBookclubPlaceFilterBinding) {
        // bookstore 필터 상태 업데이트
        binding.placeFilterBookstoreIv.background =
            if (selectedFilterId == binding.placeFilterBookstoreIv.id) {
                binding.root.context.getDrawable(R.drawable.btn_filter_bookstore_selected)
            } else {
                binding.root.context.getDrawable(R.drawable.btn_filter_bookstore)
            }

        // bookcafe 필터 상태 업데이트
        binding.placeFilterBookcafeIv.background =
            if (selectedFilterId == binding.placeFilterBookcafeIv.id) {
                binding.root.context.getDrawable(R.drawable.btn_filter_bookcafe_selected)
            } else {
                binding.root.context.getDrawable(R.drawable.btn_filter_bookcafe)
            }
    }
}
