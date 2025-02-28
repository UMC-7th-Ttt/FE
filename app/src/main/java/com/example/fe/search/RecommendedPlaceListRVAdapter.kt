package com.example.fe.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.JohnRetrofitClient
import com.example.fe.R
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.databinding.ItemRecommendedPlaceBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import com.example.fe.scrap.api.ScrapAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendedPlaceListRVAdapter(
    private val context: Context, // Context 전달
    private val placeList: List<PlaceResponse>,
    private val onItemClick: (PlaceResponse) -> Unit
) : RecyclerView.Adapter<RecommendedPlaceListRVAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemRecommendedPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceResponse) {
            binding.itemRecommendedPlaceNameTv.text = place.title
            binding.itemRecommendedPlaceRatingTv.text = String.format("%.1f", place.totalRating)

            // 이미지 설정 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 기본 이미지 설정
                .into(binding.itemRecommendedPlaceImg)

            // 카테고리 변환 ("BOOKSTORE" -> "서점", "CAFE" -> "북카페")
            binding.itemRecommendedPlaceTagTv.text = when (place.category) {
                "BOOKSTORE" -> "서점"
                "CAFE" -> "북카페"
                else -> place.category
            }

            // 북마크 상태 설정
            updateBookmarkUI(place.isScraped)

            // 장소 클릭 이벤트 (상세보기로 이동)
            binding.itemRecommendedPlaceImg.setOnClickListener {
                onItemClick(place)
            }

            // 북마크 버튼 클릭 이벤트
            binding.itemRecommendedPlaceBookmarkIv.setOnClickListener {
                if (place.isScraped) {
                    deleteScrap(place)
                } else {
                    showScrapBottomSheet(place)
                }
            }
        }

        // 북마크 상태 UI 업데이트
        private fun updateBookmarkUI(isScraped: Boolean) {
            binding.itemRecommendedPlaceBookmarkIv.setImageResource(
                if (isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }

        // 북마크(스크랩) 삭제 API 호출
        private fun deleteScrap(place: PlaceResponse) {
            val api = JohnRetrofitClient.getClient(context).create(ScrapAPI::class.java)
            api.deletePlaceScrap(place.placeId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            place.isScraped = false // 스크랩 해제
                            updateBookmarkUI(false)

                            // LayoutInflater 수정
                            val inflater = LayoutInflater.from(binding.root.context)
                            val toastBinding = FragmentScrapCancelCustomToastBinding.inflate(inflater)

                            // 스크랩 취소 토스트 메시지 설정
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

        // 스크랩 추가 바텀시트 띄우기
        private fun showScrapBottomSheet(place: PlaceResponse) {
            val scrapBottomSheet = ScrapBottomSheetFragment(
                bookId = null,  // 장소 스크랩이므로 bookId는 null
                placeId = place.placeId,
                onBookmarkStateChanged = { isSelected ->
                    place.isScraped = isSelected
                    updateBookmarkUI(isSelected)
                }
            )
            scrapBottomSheet.show(
                (binding.root.context as AppCompatActivity).supportFragmentManager,
                scrapBottomSheet.tag
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRecommendedPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(placeList[position])
    }

    override fun getItemCount(): Int = placeList.size
}