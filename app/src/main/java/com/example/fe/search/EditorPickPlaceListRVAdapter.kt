package com.example.fe.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemEditorPickPlaceBinding
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditorPickPlaceListRVAdapter(
    private val placeList: List<PlaceResponse>,
    private val onItemClick: (PlaceResponse) -> Unit // 클릭 이벤트 추가
) :
    RecyclerView.Adapter<EditorPickPlaceListRVAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(private val binding: ItemEditorPickPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceResponse) {
            // 공간 이미지 설정 (Glide 사용)
            Glide.with(binding.root.context)
                .load(place.image)
                .placeholder(R.drawable.img_place1) // 기본 이미지 설정
                .into(binding.itemEditorPickPlaceIv)

            // 공간 이름 설정
            binding.itemEditorPickPlaceTitleTv.text = place.title

            // 카테고리 변환 ("BOOKSTORE" -> "서점", "CAFE" -> "북카페")
            binding.itemEditorPickCategoryTv.text = when (place.category) {
                "BOOKSTORE" -> "서점"
                "CAFE" -> "북카페"
                else -> place.category
            }

            // 큐레이션 한 마디 설정 (카테고리에 따라 앞에 아이콘 추가)
            val curationPrefix = when (place.category) {
                "BOOKSTORE" -> "📙 "
                "CAFE" -> "☕️ "
                else -> ""
            }
            binding.itemCurationTitleTv.text = "$curationPrefix \"${place.curationTitle}\""

            // 장소 클릭 시 상세 페이지 이동 추가
            binding.itemEditorPickPlaceIv.setOnClickListener {
                onItemClick(place)
            }

            // 북마크 상태 설정
            updateBookmarkUI(place.isScraped)

            // 북마크 버튼 클릭 이벤트
            binding.itemBookmarkIv.setOnClickListener {
                if (place.isScraped) {
                    deleteScrap(place)
                } else {
                    showScrapBottomSheet(place)
                }
            }
        }

        // 북마크 상태 UI 업데이트
        private fun updateBookmarkUI(isScraped: Boolean) {
            binding.itemBookmarkIv.setImageResource(
                if (isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }

        // 북마크(스크랩) 삭제 API 호출
        private fun deleteScrap(place: PlaceResponse) {
            RetrofitClient.scrapApi.deletePlaceScrap(place.placeId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            place.isScraped = false // 스크랩 해제
                            updateBookmarkUI(false)

                            // LayoutInflater 수정
                            val inflater = LayoutInflater.from(binding.root.context)
                            val toastBinding = FragmentScrapCancelCustomToastBinding.inflate(inflater)

                            // 토스트 메시지 설정
                            toastBinding.scrapCancelTv.text = "스크랩이 취소됨"

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
        val binding = ItemEditorPickPlaceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(placeList[position])
    }

    override fun getItemCount(): Int = placeList.size
}
