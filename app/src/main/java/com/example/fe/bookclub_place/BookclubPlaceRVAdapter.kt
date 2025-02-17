package com.example.fe.bookclub_place

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.R
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentScrapCustomToastBinding
import com.example.fe.databinding.ItemBookclubPlaceBinding
import com.example.fe.databinding.ItemBookclubPlaceFilterBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceRVAdapter(
    private val places: List<PlaceResponse>,
    private val onItemClick: (PlaceResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_FILTER = 0
        const val VIEW_TYPE_PLACE = 1
    }

    private var selectedFilterId: Int? = null
    private var filteredPlaces: List<PlaceResponse> = places // 필터링된 장소 리스트

    inner class FilterViewHolder(val binding: ItemBookclubPlaceFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PlaceViewHolder(val binding: ItemBookclubPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceResponse) {
            binding.itemBookclubPlaceNameTv.text = place.title

            if (place.category == "BOOKSTORE") {
                binding.itemBookclubPlaceTagTv.text = "서점"
            } else {
                binding.itemBookclubPlaceTagTv.text = "카페"
            }

            binding.itemBookclubPlaceRatingTv.text = String.format("%.1f", place.totalRating)

            // 이미지 URL을 사용하여 이미지 로드 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 기본 이미지
                .into(binding.itemBookclubPlaceImg)

            // 북마크 상태에 따른 아이콘 변경
            updateBookmarkUI(place.isScraped)

            // 북마크 클릭 이벤트
            binding.itemBookclubPlaceBookmarkIc.setOnClickListener {
                if (place.isScraped) {
                    deleteScrap(place)
                } else {
                    val scrapBottomSheet = ScrapBottomSheetFragment(
                        bookId = null,
                        placeId = place.placeId,
                        onBookmarkStateChanged = { isSelected ->
                            place.isScraped = isSelected // API 응답값으로 업데이트
                            updateBookmarkUI(isSelected)
                        }
                    )
                    scrapBottomSheet.show(
                        (binding.root.context as AppCompatActivity).supportFragmentManager,
                        scrapBottomSheet.tag
                    )
                }
            }

            // 상세 페이지 이동
            binding.itemBookclubPlaceImg.setOnClickListener {
                onItemClick(place)
            }
        }

        // 북마크 UI 업데이트 함수
//        private fun updateBookmarkUI(isScraped: Boolean) {
//            binding.itemBookclubPlaceBookmarkIc.setImageResource(
//                if (isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
//            )
//        }

        private fun updateBookmarkUI(isScraped: Boolean) {
            binding.root.context?.let { context ->
                binding.itemBookclubPlaceBookmarkIc.setImageResource(
                    if (isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
                )
            }
        }


        // 스크랩 삭제 API 호출
        private fun deleteScrap(place: PlaceResponse) {
            Log.d("ScrapAPI", "🔹 스크랩 취소 요청: placeId = ${place.placeId}")

            RetrofitClient.scrapApi.deletePlaceScrap(place.placeId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("ScrapAPI", "✅ 스크랩 취소 성공: placeId = ${place.placeId}")
                        place.isScraped = false
                        updateBookmarkUI(false)
                        showToast("스크랩이 취소되었습니다")
                    } else {
                        Log.e("ScrapAPI", "❌ 스크랩 취소 실패: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("ScrapAPI", "❌ 네트워크 오류: ${t.message}")
                }
            })
        }

        // 토스트 메시지 표시 함수
        private fun showToast(message: String) {
            Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
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

                // 필터 키워드 선택
                placeFilterBookstoreIv.setOnClickListener { handleFilterClick(it.id, this, "BOOKSTORE") }
                placeFilterBookcafeIv.setOnClickListener { handleFilterClick(it.id, this, "CAFE") }
                updateFilterState(this)
            }
        } else if (holder is PlaceViewHolder) {
            holder.bind(filteredPlaces[position - 1])
        }
    }

    override fun getItemCount(): Int = filteredPlaces.size + 1

    private fun handleFilterClick(filterId: Int, binding: ItemBookclubPlaceFilterBinding, category: String) {
        selectedFilterId = if (selectedFilterId == filterId) null else filterId
        filteredPlaces = if (selectedFilterId == null) {
            places
        } else {
            places.filter { it.category == category }
        }
        updateFilterState(binding)
        notifyDataSetChanged() // 리스트 업데이트
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