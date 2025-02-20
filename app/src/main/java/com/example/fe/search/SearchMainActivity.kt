package com.example.fe.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.R
import com.example.fe.databinding.ActivitySearchMainBinding

class SearchMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchMainBinding
    private lateinit var recentSearchAdapter: RecentSearchRVAdapter
    private val recentSearches = mutableListOf<String>()

    private var callerActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 호출한 액티비티 정보를 저장
        callerActivity = intent.getStringExtra("CALLER")

        initBackBtnClickListener()
        initSearchInputListener()
        initBookclubPlaceRecentSearchRV()
        initCategoryClickListener()
        initDeleteAllButtonListener()
    }

    private fun initBackBtnClickListener() {
        binding.searchMainBackBtn.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.search_main_layout)

            if (fragment is SearchResultFragment) {
                // 검색 결과 프래그먼트가 있으면 제거하고 기본 화면 유지
                supportFragmentManager.popBackStack()
            } else {
                // 검색 결과 프래그먼트가 없으면 SearchMainActivity 종료
                finish()
            }
        }
    }


    private fun initSearchInputListener() {
        binding.searchMainInputEt.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = binding.searchMainInputEt.text.toString().trim()
                if (keyword.isNotEmpty()) {
                    RecentSearchManager.addRecentSearch(this, keyword)
                    recentSearches.clear()
                    recentSearches.addAll(RecentSearchManager.getRecentSearches(this))
                    recentSearchAdapter.notifyDataSetChanged()
                    UpdateUIWithEmptyRecentSearch()

                    // 키보드 숨기기
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                    // EditText의 포커스 제거 (커서 깜빡임 방지)
                    binding.searchMainInputEt.clearFocus()

                    // 검색 결과 프래그먼트로 이동
                    val fragment = SearchResultFragment().apply {
                        arguments = Bundle().apply {
                            putString("KEYWORD", keyword)
                        }
                    }

                    supportFragmentManager.commit {
                        replace(R.id.search_main_layout, fragment)
                        addToBackStack(null)
                    }
                }
                true
            } else {
                false
            }
        }
    }

    private fun initBookclubPlaceRecentSearchRV() {
        recentSearches.addAll(RecentSearchManager.getRecentSearches(this))
        recentSearchAdapter = RecentSearchRVAdapter(recentSearches) { keyword ->
            RecentSearchManager.removeRecentSearch(this, keyword)
            recentSearches.remove(keyword)
            recentSearchAdapter.notifyDataSetChanged()
            UpdateUIWithEmptyRecentSearch()
        }
        binding.recentSearchListRv.adapter = recentSearchAdapter
        binding.recentSearchListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        UpdateUIWithEmptyRecentSearch()
    }

    // 최근 검색어 전체 삭제 버튼 클릭 이벤트 처리
    private fun initDeleteAllButtonListener() {
        binding.recentSearchDeleteTv.setOnClickListener {
            RecentSearchManager.clearRecentSearches(this)
            recentSearches.clear()
            recentSearchAdapter.notifyDataSetChanged()
            UpdateUIWithEmptyRecentSearch()
        }
    }

    // 최근 검색어 리스트 상태에 맞춰 UI조정
    private fun UpdateUIWithEmptyRecentSearch() {
        if (recentSearches.isEmpty()) {
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
            constraintSet.clear(binding.recommendedSearchTv.id, ConstraintSet.START)
            constraintSet.clear(binding.koreanLiteratureCv.id, ConstraintSet.TOP)
            constraintSet.clear(binding.koreanLiteratureCv.id, ConstraintSet.START)

            // "추천 검색어"를 최상단으로 이동하고 부모의 START에 연결
            constraintSet.connect(
                binding.recommendedSearchTv.id,
                ConstraintSet.TOP,
                constraintLayout.id,
                ConstraintSet.TOP,
                40
            )
            constraintSet.connect(
                binding.recommendedSearchTv.id,
                ConstraintSet.START,
                constraintLayout.id,
                ConstraintSet.START
            )
            // 왼쪽 마진 적용
            constraintSet.setMargin(binding.recommendedSearchTv.id, ConstraintSet.START, 50)

            // 카드뷰도 부모의 START에 연결한 후 마진 추가
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
                constraintLayout.id,
                ConstraintSet.START
            )
            // 왼쪽 마진 적용
            constraintSet.setMargin(binding.koreanLiteratureCv.id, ConstraintSet.START, 50)

            constraintSet.applyTo(constraintLayout)
        } else {
            binding.recentSearchTv.visibility = View.VISIBLE
            binding.recentSearchDeleteTv.visibility = View.VISIBLE
            binding.recentSearchListRv.visibility = View.VISIBLE
        }
    }

    private fun initCategoryClickListener() {
        val categoryMap = mapOf(
            binding.koreanLiteratureCv to "한국 문학",
            binding.humanityCv to "인문",
            binding.selfDevelopmentCv to "자기 계발",
            binding.essayAndTravelCv to "에세이/여행",
            binding.societyAndNaturalScienceCv to "사회/자연 과학",
            binding.worldLiteratureCv to "세계 문학"
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
                .replace(R.id.main_layout, RecommendedSearchPlaceDetailFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}