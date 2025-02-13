package com.example.fe.bookclub_place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.*
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.bookclub_place.api.PlaceSearchResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentBookclubPlaceListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceListFragment : Fragment() {

    private lateinit var binding: FragmentBookclubPlaceListBinding
    private lateinit var adapter: BookclubPlaceRVAdapter
    private val places = mutableListOf<PlaceResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceListBinding.inflate(inflater, container, false)

        // `LAT`, `LON` 또는 `KEYWORD` 값 확인
        val lat = arguments?.getDouble("LAT", -1.0) ?: -1.0
        val lon = arguments?.getDouble("LON", -1.0) ?: -1.0
        val keyword = arguments?.getString("KEYWORD", "")

        Log.d("BookclubPlaceListFragment", "✅ 전달받은 데이터 - LAT: $lat, LON: $lon, KEYWORD: $keyword")

        initBookclubPlaceListRV()

        if (!keyword.isNullOrBlank()) { // keyword가 null이 아니고 공백이 아닐 때만 실행
            searchPlaces(keyword)
        } else if (lat != -1.0 && lon != -1.0) {
            sortPlaces(lat, lon) // 현재 위치 기반 장소 검색
        }

        return binding.root
    }

    private fun initBookclubPlaceListRV() {
        adapter = BookclubPlaceRVAdapter(places) { place ->
            val fragment = BookclubPlaceDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("PLACE_ID", place.placeId) // placeId 전달
                }
            }
            (requireActivity() as MainActivity).addFragment(
                fragment,
                showBottomNav = false
            )
        }
        binding.bookclubPlaceRv.layoutManager = LinearLayoutManager(requireContext())
        binding.bookclubPlaceRv.adapter = adapter
    }

    private fun searchPlaces(keyword: String) {
        RetrofitClient.placeApi.searchPlaces(keyword).enqueue(object : Callback<PlaceSearchResponse> {
            override fun onResponse(
                call: Call<PlaceSearchResponse>,
                response: Response<PlaceSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val newPlaces = response.body()?.result?.places ?: emptyList()
                    Log.d("BookclubPlaceListFragment", "✅ 검색 결과: $newPlaces")

                    places.clear()
                    places.addAll(newPlaces)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("BookclubPlaceListFragment", "❌ 응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
                Log.e("BookclubPlaceListFragment", "❌ 네트워크 오류: ${t.message}")
            }
        })
    }

    private fun sortPlaces(lat: Double, lon: Double) {
        RetrofitClient.placeApi.sortPlaces(lat, lon).enqueue(object : Callback<PlaceSearchResponse> {
            override fun onResponse(
                call: Call<PlaceSearchResponse>,
                response: Response<PlaceSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    val newPlaces = result?.places ?: emptyList()
                    val currentPlace = result?.currentPlace ?: "알 수 없음" // currentPlace 가져오기

                    Log.d("BookclubPlaceListFragment", "✅ 현재 위치 기반 검색 결과: $newPlaces")
                    Log.d("BookclubPlaceListFragment", "📍 현재 위치 (currentPlace): $currentPlace")

                    places.clear()
                    places.addAll(newPlaces)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("BookclubPlaceListFragment", "❌ 응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
                Log.e("BookclubPlaceListFragment", "❌ 네트워크 오류: ${t.message}")
            }
        })
    }
}