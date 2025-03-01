package com.example.fe.bookclub_place

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.JohnRetrofitClient
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.R
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.databinding.ItemBookclubPlaceBinding
import com.example.fe.databinding.ItemBookclubPlaceFilterBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import com.example.fe.scrap.api.ScrapAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceRVAdapter(
    private val context: Context, // Context 전달
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
        private fun updateBookmarkUI(isScraped: Boolean) {
            binding.itemBookclubPlaceBookmarkIc.setImageResource(
                if (isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }

        // 스크랩 삭제 API 호출
        private fun deleteScrap(place: PlaceResponse) {

//            RetrofitClient.scrapApi.deletePlaceScrap(place.placeId).enqueue(object : Callback<Void>

            val api = JohnRetrofitClient.getClient(context).create(ScrapAPI::class.java)
            api.deletePlaceScrap(place.placeId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        place.isScraped = false
                        updateBookmarkUI(false)

                        // LayoutInflater 수정
                        val inflater = LayoutInflater.from(binding.root.context)
                        val toastBinding = FragmentScrapCancelCustomToastBinding.inflate(inflater)

                        // 토스트 메시지 설정
                        toastBinding.scrapCancelTv.text = "스크랩 취소되었습니다!"

                        // 커스텀 토스트 생성 및 표시
                        val toast = Toast(binding.root.context).apply {
                            duration = Toast.LENGTH_SHORT
                            view = toastBinding.root
                            setGravity(android.view.Gravity.TOP, 0, 100)
                        }
                        toast.show()

                    } else {
                        Log.e("ScrapAPI", "❌ 스크랩 취소 실패: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("ScrapAPI", "❌ 네트워크 오류: ${t.message}")
                }
            })
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

    // 필터 적용
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilterViewHolder) {
            holder.binding.apply {
                // 초기 필터 기본값 (거리순)
                var currentFilter = "거리순"

                placeFilterSelectedIv.setOnClickListener {
                    val filterBottomSheet = BookclubPlaceFilterBottomSheetFragment(currentFilter) { selectedFilter ->
                        currentFilter = selectedFilter // 선택한 필터값 저장

                        Log.d("BookclubPlaceRVAdapter", "📌 필터 선택됨: $selectedFilter")

                        // 선택한 필터에 따라 placeFilterSelectedIv 배경 변경
                        placeFilterSelectedIv.setBackgroundResource(
                            if (selectedFilter == "추천순") R.drawable.btn_filter_recommendation
                            else R.drawable.btn_filter_distance
                        )
                        // ✅ 부모 Fragment에서 직접 `updateListByFilter` 호출
                        val fragment = holder.itemView.context as? AppCompatActivity
                        fragment?.supportFragmentManager?.fragments?.forEach { frag ->
                            if (frag is BookclubPlaceFragment) {
                                Log.d("BookclubPlaceRVAdapter", "✅ BookclubPlaceFragment에 필터 전달 성공")
                                frag.updateListByFilter(selectedFilter)
                            }
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