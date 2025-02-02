package com.example.fe.bookclub_place

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.Place
import com.example.fe.PlaceSearchResponse
import com.example.fe.R
import com.example.fe.RetrofitInstance
import com.example.fe.search.RecentSearchRVAdapter
import com.example.fe.databinding.ActivityBookclubPlaceSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubPlaceSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubPlaceSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBackBtnClickListener()
        initBookclubPlaceRecentSearchRV()
        initBookclubRecommendedPlaceRV()

        searchPlaces("카페") // ✅ 예제: "카페" 키워드로 검색

    }

    private fun searchPlaces(keyword: String) {
        RetrofitInstance.api.searchPlaces(keyword).enqueue(object : Callback<PlaceSearchResponse> {
            override fun onResponse(
                call: Call<PlaceSearchResponse>,
                response: Response<PlaceSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("BookclubPlaceSearchActivity", "✅ 검색 결과: $responseBody")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("BookclubPlaceSearchActivity", "❌ 응답 실패: ${response.code()}, 오류 본문: $errorBody")

                    // ✅ 서버가 JSON이 아닌 HTML을 반환하는지 확인
                    if (errorBody?.startsWith("<!DOCTYPE html>") == true) {
                        Log.e("BookclubPlaceSearchActivity", "⚠️ 서버가 HTML 오류 페이지를 반환함! 백엔드에 문의 필요")
                    }
                }
            }

            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
                Log.e("BookclubPlaceSearchActivity", "❌ 네트워크 오류: ${t.message}")
            }
        })
    }


//    private fun searchPlaces(keyword: String) {
//        RetrofitInstance.api.searchPlaces(keyword).enqueue(object : Callback<PlaceSearchResponse> {
//            override fun onResponse(
//                call: Call<PlaceSearchResponse>,
//                response: Response<PlaceSearchResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val places = response.body()?.result?.places ?: emptyList()
//                    Log.d("BookclubPlaceSearchActivity", "검색 결과: $places")
//                } else {
//                    Log.e("BookclubPlaceSearchActivity", "응답 실패: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
//                Log.e("BookclubPlaceSearchActivity", "네트워크 오류: ${t.message}")
//            }
//        })
//    }

    private fun initBackBtnClickListener() {
        binding.bookclubPlaceSearchBackBtn.setOnClickListener {
            finish()
        }
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
