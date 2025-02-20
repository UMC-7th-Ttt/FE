package com.example.fe.bookclub_place

import HomeFragment
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.fe.JohnRetrofitClient
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.Review.SpaceReviewActivity
import com.example.fe.bookclub_place.api.PlaceDetail
import com.example.fe.bookclub_place.api.PlaceDetailResponse
import com.example.fe.bookclub_place.api.PlaceSearchAPI
import com.example.fe.databinding.FragmentBookclubPlaceDetailBinding
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import com.example.fe.scrap.api.ScrapAPI
import com.example.fe.search.SearchMainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceDetailFragment : DialogFragment() {

    private lateinit var binding: FragmentBookclubPlaceDetailBinding
    private var isBookmarked = false // 북마크 상태 추적
    private var placeId: Int = -1 // 장소 ID 저장
    private var placeDetail: PlaceDetail? = null  // 장소 상세 데이터 저장용 변수

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
        initHomeBtnClickListener()
        initBookmarkClickListener()
        initReviewWriteClickListener()
        return binding.root
    }

    private fun initBackBtnClickListener() {
        binding.bookclubPlaceBackIv.setOnClickListener {
            parentFragmentManager.popBackStack() // 기본 뒤로 가기
        }
    }

    private fun initHomeBtnClickListener() {
        binding.bookclubPlaceDetailHomeIv.setOnClickListener {
            if (requireActivity() is SearchMainActivity) {
                // SearchMainActivity에서 왔다면 MainActivity를 새로 실행
                val mainActivityIntent = Intent(requireActivity(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    putExtra("GO_HOME", true)
                }
                startActivity(mainActivityIntent)

                // 현재 SearchMainActivity 종료
                requireActivity().finish()
            } else {
                // MainActivity에서 왔다면 Fragment 교체
                val homeFragment = HomeFragment()
                requireActivity().supportFragmentManager.commit {
                    replace(R.id.main_frm_in_bottom_nav, homeFragment)
                }

                // 바텀 네비게이션 업데이트
                (activity as? MainActivity)?.binding?.bottomNavigation?.selectedItemId = R.id.bottom_nav_home

                // DialogFragment 닫기
                dismiss()
            }
        }
    }

    private fun initReviewWriteClickListener() {
        binding.bookclubPlaceDetailWriteIv.setOnClickListener {
            val context = binding.root.context
            val intent = Intent(context, SpaceReviewActivity::class.java).apply {
                putExtra("PLACE_ID", placeDetail?.placeId ?: -1)  // 장소 ID 전달
                putExtra("PLACE_TITLE", placeDetail?.title ?: "알 수 없음") // 장소 제목 전달
                putExtra("PLACE_IMAGE", placeDetail?.image ?: "") // 장소 이미지 URL 전달
            }
            context.startActivity(intent)
        }
    }

    private fun getPlaceDetails(placeId: Int) {
//        RetrofitClient.placeApi.getPlaceDetails(placeId).enqueue(object : Callback<PlaceDetailResponse>

        val api = JohnRetrofitClient.getClient(requireContext()).create(PlaceSearchAPI::class.java)
        api.getPlaceDetails(placeId).enqueue(object : Callback<PlaceDetailResponse> {
            override fun onResponse(
                call: Call<PlaceDetailResponse>,
                response: Response<PlaceDetailResponse>
            ) {
                if (response.isSuccessful) {
                    placeDetail = response.body()?.result
                    placeDetail?.let {
                        Glide.with(this@BookclubPlaceDetailFragment)
                            .load(it.image)
                            .placeholder(R.drawable.img_place1)
                            .into(binding.bookclubPlaceDetailImgIv)

                        binding.bookclubPlaceDetailNameTv.text = it.title
                        binding.bookclubPlaceDetailTagTv.text = if (it.category == "CAFE") "북카페" else "서점"
                        if(it.category == "CAFE") {
                            binding.bookclubPlaceDetailDescriptionTitleTv.text = it.curationTitle ?: "햇빛과 함께 사색을 즐겨보아요."
                            binding.bookclubPlaceDetailIntroTv.text = it.curationContent ?: "봄과 가까워지고 있는 지금, 점점 나들이 가도 좋은 날씨가 되고 있어요. 오전에 잠시 카페에 들러 커피와 함께 독서 시작해 볼까요?"
                        } else {
                            binding.bookclubPlaceDetailDescriptionTitleTv.text = it.curationTitle ?: "취향의 공간에서 즐기는 소확행 시간"
                            binding.bookclubPlaceDetailIntroTv.text = it.curationContent ?: "온전히 책과 함께할 수 있는 공간에서 나만의 취향을 찾아봐요. 온전히 나와 함께하는 힐링 시간을 보낼 수 있을 거예요."
                        }
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
//        RetrofitClient.scrapApi.deletePlaceScrap(placeId).enqueue(object : Callback<Void>

        val api = JohnRetrofitClient.getClient(requireContext()).create(ScrapAPI::class.java)
        api.deletePlaceScrap(placeId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isBookmarked = false
                    updateBookmarkUI()

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

    companion object {
        @JvmStatic
        fun newInstance(placeId: Int) = BookclubPlaceDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("PLACE_ID", placeId)
            }
        }
    }
}

