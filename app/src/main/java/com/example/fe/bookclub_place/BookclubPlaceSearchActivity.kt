package com.example.fe.bookclub_place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.MainActivity
import com.example.fe.Place
import com.example.fe.R
import com.example.fe.databinding.ActivityBookclubPlaceSearchBinding
import com.example.fe.search.RecentSearchRVAdapter

class BookclubPlaceSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubPlaceSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubPlaceSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBackBtnClickListener()
        initBookclubPlaceRecentSearchRV()
        initBookclubRecommendedPlaceRV()
        initSearchInputListener()
        initDeleteAllButtonListener() // 전체 삭제 버튼 이벤트 추가
    }

    private fun initSearchInputListener() {
        binding.bookclubPlaceSearchInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = binding.bookclubPlaceSearchInputEt.text.toString().trim()

                if (keyword.isNotEmpty()) {
                    Log.d("BookclubPlaceSearchActivity", "✅ 전달할 키워드: $keyword")

                    // ✅ 결과 데이터 설정 (BookclubPlaceFragment로 전달)
                    val resultIntent = Intent().apply {
                        putExtra("KEYWORD", keyword)
                    }
                    setResult(RESULT_OK, resultIntent) // ✅ 결과 전달
                    finish() // ✅ 현재 Activity 종료 (이전 Fragment로 돌아감)
                }
                true
            } else {
                false
            }
        }
    }

    private fun initBackBtnClickListener() {
        binding.bookclubPlaceSearchBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun initBookclubPlaceRecentSearchRV() {
        val recentSearches = mutableListOf("북카페", "공간 대여", "커피", "서점", "화장실", "성북구", "디저트")

        val adapter = RecentSearchRVAdapter(recentSearches)
        binding.bookclubPlaceRecentSearchListRv.adapter = adapter
        binding.bookclubPlaceRecentSearchListRv.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun initBookclubRecommendedPlaceRV() {
        val recommendedPlaces = listOf(
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1, false),
            Place("서울책보고", "서점", 4.5, R.drawable.img_place2, false),
            Place("카페꼼마 합정점", "카페", 4.7, R.drawable.img_place3, false),
            Place("전부책방스튜디오", "서점", 4.3, R.drawable.img_place4, false),
            Place("알키미스타", "카페", 4.6, R.drawable.img_place5, false)
        )

        val recommendedPlaceAdapter = BookclubRecommendedPlaceRVAdapter(recommendedPlaces)
        binding.bookclubRecommendedPlaceListRv.adapter = recommendedPlaceAdapter
        binding.bookclubRecommendedPlaceListRv.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    // 전체 삭제 버튼 클릭 이벤트 처리
    private fun initDeleteAllButtonListener() {
        binding.bookclubPlaceRecentSearchDeleteTv.setOnClickListener {
            // RecyclerView Adapter에서 데이터 삭제
            val adapter = binding.bookclubPlaceRecentSearchListRv.adapter as? RecentSearchRVAdapter
            adapter?.clearData()

            // 최근 검색어 관련 UI 숨기기
            binding.bookclubPlaceRecentSearchTv.visibility = View.GONE
            binding.bookclubPlaceRecentSearchDeleteTv.visibility = View.GONE
            binding.bookclubPlaceRecentSearchListRv.visibility = View.GONE

            // 추천 공간을 상단으로 이동하도록 Constraint 변경
            val constraintLayout = binding.bookclubPlaceSearchMainLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            // 추천 공간 텍스트를 최상단으로 배치
            constraintSet.connect(
                binding.bookclubRecommendedPlaceTv.id,
                ConstraintSet.TOP,
                constraintLayout.id,
                ConstraintSet.TOP,
                15 // 위쪽 마진
            )

            // 추천 공간 RecyclerView를 추천 공간 제목 아래로 이동
            constraintSet.connect(
                binding.bookclubRecommendedPlaceListRv.id,
                ConstraintSet.TOP,
                binding.bookclubRecommendedPlaceTv.id,
                ConstraintSet.BOTTOM,
                20 // 기존 마진 유지
            )

            // 변경 사항 적용
            constraintSet.applyTo(constraintLayout)
        }
    }
}