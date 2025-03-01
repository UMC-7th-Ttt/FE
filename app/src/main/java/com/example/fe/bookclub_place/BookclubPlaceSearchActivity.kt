package com.example.fe.bookclub_place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.JohnRetrofitClient
import com.example.fe.R
import com.example.fe.bookclub_place.api.PlaceSearchAPI
import com.example.fe.bookclub_place.api.PlaceSearchResponse
import com.example.fe.bookclub_place.api.PlaceSuggestionResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.ActivityBookclubPlaceSearchBinding
import com.example.fe.search.EditorPickPlaceListRVAdapter
import com.example.fe.search.RecentSearchManager
import com.example.fe.search.RecentSearchRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubPlaceSearchBinding
    private lateinit var recentSearchAdapter: RecentSearchRVAdapter
    private val recentSearches = mutableListOf<String>()

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

                    RecentSearchManager.addRecentSearch(this, keyword)
                    recentSearches.clear()
                    recentSearches.addAll(RecentSearchManager.getRecentSearches(this))
                    recentSearchAdapter.notifyDataSetChanged()
                    UpdateUIWithEmptyRecentSearch()

                    // 결과 데이터 설정 (BookclubPlaceFragment로 전달)
                    val resultIntent = Intent().apply {
                        putExtra("KEYWORD", keyword)
                    }
                    setResult(RESULT_OK, resultIntent) // 결과 전달
                    finish() // 현재 Activity 종료 (이전 Fragment로 돌아감)
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
        recentSearches.addAll(RecentSearchManager.getRecentSearches(this))
        recentSearchAdapter = RecentSearchRVAdapter(recentSearches) { keyword ->
            RecentSearchManager.removeRecentSearch(this, keyword)
            recentSearches.remove(keyword)
            recentSearchAdapter.notifyDataSetChanged()
            UpdateUIWithEmptyRecentSearch()
        }
        binding.bookclubPlaceRecentSearchListRv.adapter = recentSearchAdapter
        binding.bookclubPlaceRecentSearchListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        UpdateUIWithEmptyRecentSearch()
    }

    private fun initBookclubRecommendedPlaceRV() {
//        RetrofitClient.placeApi.getPlaceSuggestions().enqueue(object :
//            Callback<PlaceSuggestionResponse>

        val api = JohnRetrofitClient.getClient(this).create(PlaceSearchAPI::class.java)
        api.getPlaceSuggestions().enqueue(object :
            Callback<PlaceSuggestionResponse> {
            override fun onResponse(
                call: Call<PlaceSuggestionResponse>,
                response: Response<PlaceSuggestionResponse>
            ) {
                if (response.isSuccessful) {
                    val recommendedPlaces = response.body()?.result?.places ?: emptyList()
//                    val adapter = BookclubRecommendedPlaceRVAdapter(recommendedPlaces)

                    // 클릭 시 상세 페이지 이동하도록 수정
                    val adapter = BookclubRecommendedPlaceRVAdapter(recommendedPlaces) { place ->
                        val fragment = BookclubPlaceDetailFragment().apply {
                            arguments = Bundle().apply {
                                putLong("PLACE_ID", place.placeId) // 장소 ID 전달
                            }
                        }

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_bookclub_place_search_layout, fragment) // 컨테이너 맞춰서 변경
                            .addToBackStack(null)
                            .commit()
                    }

                    binding.bookclubRecommendedPlaceListRv.adapter = adapter
                    binding.bookclubRecommendedPlaceListRv.layoutManager =
                        LinearLayoutManager(this@BookclubPlaceSearchActivity, RecyclerView.HORIZONTAL, false)
                } else {
                    Log.e("API Error", "❌ ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PlaceSuggestionResponse>, t: Throwable) {
                Log.e("Network Error", "❌ ${t.message}")
            }
        })
    }

    // 최근 검색어 전체 삭제 버튼 클릭 이벤트 처리
    private fun initDeleteAllButtonListener() {
        binding.bookclubPlaceRecentSearchDeleteTv.setOnClickListener {
            RecentSearchManager.clearRecentSearches(this)
            recentSearches.clear()
            recentSearchAdapter.notifyDataSetChanged()
            UpdateUIWithEmptyRecentSearch()
        }
    }

    // 최근 검색어 리스트 상태에 맞춰 UI 조정
    private fun UpdateUIWithEmptyRecentSearch() {
        if (recentSearches.isEmpty()) {
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
        } else {
            binding.bookclubPlaceRecentSearchTv.visibility = View.VISIBLE
            binding.bookclubPlaceRecentSearchDeleteTv.visibility = View.VISIBLE
            binding.bookclubPlaceRecentSearchListRv.visibility = View.VISIBLE
        }
    }
}