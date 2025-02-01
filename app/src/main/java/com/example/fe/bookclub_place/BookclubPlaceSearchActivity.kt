package com.example.fe.bookclub_place

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.Place
import com.example.fe.R
import com.example.fe.search.RecentSearchRVAdapter
import com.example.fe.databinding.ActivityBookclubPlaceSearchBinding

class BookclubPlaceSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubPlaceSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubPlaceSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookclubPlaceRecentSearchRV()
        initBookclubRecommendedPlaceRV()
    }

    private fun initBookclubPlaceRecentSearchRV() {
        val recentSearches = listOf("북카페", "공간 대여", "커피", "서점", "화장실", "성북구", "디저트")

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
}
