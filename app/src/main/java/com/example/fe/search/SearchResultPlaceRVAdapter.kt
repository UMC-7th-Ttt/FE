package com.example.fe.search

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.Review.SpaceReviewActivity
import com.example.fe.databinding.ItemSearchResultPlaceBinding
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultPlaceRVAdapter(
    private val places: List<PlaceResponse>,
    private val onItemClick: (PlaceResponse) -> Unit
) : RecyclerView.Adapter<SearchResultPlaceRVAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSearchResultPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: PlaceResponse) {
            binding.itemSearchResultPlaceNameTv.text = place.title

            // 평점 소수점 한자리까지
            binding.itemSearchResultPlaceRatingTv.text = String.format("%.1f", place.totalRating)

            // 이미지 로딩 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 기본 이미지
                .into(binding.itemSearchResultPlaceIv)

            // 장소 태그
            binding.itemSearchResultPlaceTagTv.text =
                if (place.category == "BOOKSTORE") "서점" else "카페"

            // 북마크 상태 설정
            updateBookmarkUI(place.isScraped)

            // 북마크 버튼 클릭 이벤트 추가
            binding.itemSearchResultPlaceBookmarkIc.setOnClickListener {
                if (place.isScraped) {
                    deleteScrap(place)
                } else {
                    showScrapBottomSheet(place)
                }
            }

            // 아이템 클릭 시 이동하는 화면 결정
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intentCaller = (context as? AppCompatActivity)?.intent?.getStringExtra("CALLER") // 🔥 CALLER 값 가져오기

                if (intentCaller == "ReviewActivity") {
                    // ReviewActivity에서 왔을 때 SpaceReviewActivity로 이동
                    val intent = Intent(context, SpaceReviewActivity::class.java).apply {
                        putExtra("PLACE_ID", place.placeId) // 장소 ID 전달
                        putExtra("PLACE_TITLE", place.title)    // 장소 이름 전달
                        putExtra("PLACE_IMAGE", place.image)    // 장소 이미지 URL 전달
                    }
                    context.startActivity(intent)
                } else {
                    // 일반 검색 시 BookclubPlaceDetailFragment로 이동 (Intent가 아닌 FragmentTransaction 사용)
                    onItemClick(place)
                }
            }
        }

        private fun updateBookmarkUI(isScraped: Boolean) {
            binding.itemSearchResultPlaceBookmarkIc.setImageResource(
                if (isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }

        private fun deleteScrap(place: PlaceResponse) {
            RetrofitClient.scrapApi.deletePlaceScrap(place.placeId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            place.isScraped = false // 스크랩 해제
                            updateBookmarkUI(false)

                            val inflater = LayoutInflater.from(binding.root.context)
                            val toastBinding = FragmentScrapCancelCustomToastBinding.inflate(inflater)

                            // 스크랩 취소 토스트 메시지 설정
                            toastBinding.scrapCancelTv.text = "스크랩 취소되었습니다!"

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int = places.size
}
