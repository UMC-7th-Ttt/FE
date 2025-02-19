package com.example.fe.bookclub_place

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.bookclub_place.api.PlaceDetailResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentBookclubPlaceDetailBinding
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceDetailFragment : DialogFragment() {

    private lateinit var binding: FragmentBookclubPlaceDetailBinding
    private var isBookmarked = false // 북마크 상태 추적
    private var placeId: Int = -1 // 장소 ID 저장

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceDetailBinding.inflate(inflater, container, false)

        // Fullscreen 설정
        requireActivity().window.apply {
            statusBarColor = Color.TRANSPARENT // 상태바를 투명하게
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 상태바 위로 레이아웃 확장
                    )
        }

        // placeId 받아와 해당 상세 내용 출력
        placeId = arguments?.getInt("PLACE_ID", -1) ?: -1
        if (placeId != -1) {
            getPlaceDetails(placeId)
        }

        initBackBtnClickListener()
        initBookmarkClickListener()
        return binding.root
    }

    private fun initBackBtnClickListener() {
        binding.bookclubPlaceBackIv.setOnClickListener {
            parentFragmentManager.popBackStack() // 기본 뒤로 가기
        }
    }

    private fun getPlaceDetails(placeId: Int) {
        RetrofitClient.placeApi.getPlaceDetails(placeId).enqueue(object : Callback<PlaceDetailResponse> {
            override fun onResponse(
                call: Call<PlaceDetailResponse>,
                response: Response<PlaceDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val detail = response.body()?.result
                    detail?.let {
                        binding.bookclubPlaceDetailNameTv.text = it.title
                        binding.bookclubPlaceDetailTagTv.text = if (it.category == "CAFE") "북카페" else "서점"
                        binding.bookclubPlaceDetailDescriptionTitleTv.text = it.curationTitle ?: "야외에서도 책을 읽을 수 있는 북카페"
                        binding.bookclubPlaceDetailIntroTv.text = it.curationContent ?: "날씨가 좋은 날 여유롭게 책을 읽으며 맛있는 커피를 즐길 수 있는 뉴트로 감성의 힙한 종로 카페를 찾으시나요?"
                        binding.bookclubPlaceDetailEntireRatingNumTv.text = String.format("%.1f", it.totalRating)
                        binding.bookclubPlaceDetailLocationTv.text = it.address
                        binding.weekdaysBusinessTv.text = "운영시간 ${it.weekdaysBusiness ?: ""}"
                        binding.sunBusinessTv.text = "(일요일 ${it.sunBusiness ?: ""})"
                        binding.holidayTv.text = "정기휴무 : ${it.holiday ?: "연중무휴"}"
                        binding.bookclubPlaceDetailPhoneNumTv.text = it.phone

                        binding.bookclubPlaceDetailParkingIv.setBackgroundResource(
                            if (it.hasParking) R.drawable.kwd_parking_ok else R.drawable.kwd_parking_no
                        )

                        binding.bookclubPlaceDetailPlaceRentalIv.setBackgroundResource(
                            if (it.hasSpaceRental) R.drawable.kwd_rental_ok else R.drawable.kwd_rental_no
                        )

                        isBookmarked = it.isScraped // 북마크 상태 저장
                        updateBookmarkUI()

                        Glide.with(this@BookclubPlaceDetailFragment)
                            .load(it.image)
                            .placeholder(R.drawable.img_place1)
                            .into(binding.bookclubPlaceDetailImgIv)

                        updateStarRating(it.totalRating)
                    }
                } else {
                    Log.e("PlaceDetailFragment", "❌ 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PlaceDetailResponse>, t: Throwable) {
                Log.e("PlaceDetailFragment", "❌ 네트워크 오류: ${t.message}")
            }
        })
    }

    private fun updateStarRating(rating: Double) {
        val stars = listOf(
            binding.bookclubPlaceDetailEntireStar1,
            binding.bookclubPlaceDetailEntireStar2,
            binding.bookclubPlaceDetailEntireStar3,
            binding.bookclubPlaceDetailEntireStar4,
            binding.bookclubPlaceDetailEntireStar5
        )

        val fullStars = when {
            rating < 0.5 -> 0
            rating < 1.5 -> 1
            rating < 2.5 -> 2
            rating < 3.5 -> 3
            rating < 4.5 -> 4
            else -> 5
        }

        stars.forEachIndexed { index, star ->
            star.setImageResource(if (index < fullStars) R.drawable.ic_star else R.drawable.ic_star_empty)
        }
    }

    // 북마크 아이콘 클릭 리스너 (ScrapBottomSheetFragment에 placeId 전달)
    private fun initBookmarkClickListener() {
        binding.bookclubPlaceDetailBookmarkIv.setOnClickListener {
            if (isBookmarked) {
                deleteScrap()
            } else {
                val scrapBottomSheet = ScrapBottomSheetFragment(
                    bookId = null, // 장소 스크랩이므로 bookId는 null
                    placeId = placeId,
                    onBookmarkStateChanged = { isSelected ->
                        updateBookmarkState(isSelected)
                    }
                )
                scrapBottomSheet.show(parentFragmentManager, scrapBottomSheet.tag)
            }
        }
    }

    // 스크랩 삭제 API 호출
    private fun deleteScrap() {
        RetrofitClient.scrapApi.deletePlaceScrap(placeId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isBookmarked = false
                    updateBookmarkUI()

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

    private fun updateBookmarkUI() {
        binding.bookclubPlaceDetailBookmarkIv.setImageResource(
            if (isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
        )
    }

    private fun updateBookmarkState(isSelected: Boolean) {
        isBookmarked = isSelected
        updateBookmarkUI()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 상태바 복원
        requireActivity().window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
        }

        // 바텀 네비게이션 다시 표시
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
    }
}
