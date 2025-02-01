package com.example.fe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ActivitySearchMainBinding

class SearchMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookclubPlaceRecentSearchRV()
        initCategoryClickListener()
    }

    private fun initBookclubPlaceRecentSearchRV() {
        val recentSearches = listOf("북카페", "공간 대여", "커피", "서점", "화장실", "성북구", "디저트")

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
}