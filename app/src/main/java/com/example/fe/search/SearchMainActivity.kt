package com.example.fe.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.databinding.ActivitySearchMainBinding

class SearchMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBackBtnClickListener()
        initBookclubPlaceRecentSearchRV()
        initCategoryClickListener()
        initDeleteButtonListener()
    }

    private fun initBackBtnClickListener() {
        binding.searchMainBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun initBookclubPlaceRecentSearchRV() {
        val recentSearches = mutableListOf("북카페", "공간 대여", "커피", "서점", "화장실", "성북구", "디저트")

        val adapter = RecentSearchRVAdapter(recentSearches)
        binding.recentSearchListRv.adapter = adapter
        binding.recentSearchListRv.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun initCategoryClickListener() {
        val categoryMap = mapOf(
            binding.koreanLiteratureCv to "한국 문학",
            binding.humanityCv to "인문",
            binding.selfDevelopmentCv to "자기 계발",
            binding.essayAndTravelCv to "에세이/여행",
            binding.societyAndNaturalScienceCv to "사회/자연 과학",
            binding.worldLiteratureCv to "해외 문학"
        )

        categoryMap.forEach { (view, title) ->
            view.setOnClickListener {
                val fragment = RecommendedSearchDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString("CATEGORY_TITLE", title)
                    }
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_layout, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // bookstore_and_bookcafe_cv는 다른 프래그먼트로 이동
        binding.bookstoreAndBookcafeCv.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, RecommendedSearchPlaceDetailFragment()) // 다르게 이동
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initDeleteButtonListener() {
        binding.recentSearchDeleteTv.setOnClickListener {
            // RecyclerView Adapter에서 데이터 삭제
            val adapter = binding.recentSearchListRv.adapter as? RecentSearchRVAdapter
            adapter?.clearData()

            // 최근 검색어 관련 UI 숨기기
            binding.recentSearchTv.visibility = View.GONE
            binding.recentSearchDeleteTv.visibility = View.GONE
            binding.recentSearchListRv.visibility = View.GONE

            // 추천 검색어 및 카드뷰를 위로 이동
            val constraintLayout = binding.searchMainLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            // 기존 constraints 초기화
            constraintSet.clear(binding.recommendedSearchTv.id, ConstraintSet.TOP)
            constraintSet.clear(binding.recommendedSearchTv.id, ConstraintSet.START) // START 초기화
            constraintSet.clear(binding.koreanLiteratureCv.id, ConstraintSet.TOP)
            constraintSet.clear(binding.koreanLiteratureCv.id, ConstraintSet.START) // START 초기화

            // "추천 검색어"를 최상단으로 이동하고 부모의 `START`에 연결
            constraintSet.connect(
                binding.recommendedSearchTv.id,
                ConstraintSet.TOP,
                constraintLayout.id,  // 부모 `TOP`에 연결
                ConstraintSet.TOP,
                20 // 위쪽 마진 조정 가능
            )
            constraintSet.connect(
                binding.recommendedSearchTv.id,
                ConstraintSet.START,
                constraintLayout.id, // 부모 `START`에 연결
                ConstraintSet.START
            )
            constraintSet.setMargin(binding.recommendedSearchTv.id, ConstraintSet.START, 50) // 왼쪽 마진 적용

            // 카드뷰도 부모의 `START`에 연결한 후 마진 추가
            constraintSet.connect(
                binding.koreanLiteratureCv.id,
                ConstraintSet.TOP,
                binding.recommendedSearchTv.id,
                ConstraintSet.BOTTOM,
                40
            )
            constraintSet.connect(
                binding.koreanLiteratureCv.id,
                ConstraintSet.START,
                constraintLayout.id, // 부모 `START`에 연결
                ConstraintSet.START
            )
            constraintSet.setMargin(binding.koreanLiteratureCv.id, ConstraintSet.START, 50) // 왼쪽 마진 적용

            // 변경 사항 적용
            constraintSet.applyTo(constraintLayout)
        }
    }

}