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
import com.example.fe.bookclub_place.api.PlaceSearchAPI
import com.example.fe.bookclub_place.api.PlaceSearchResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentBookclubPlaceListBinding
import com.example.fe.search.RecentSearchManager
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

    // 상세 페이지 이동
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
        val api = JohnRetrofitClient.getClient(requireContext()).create(PlaceSearchAPI::class.java)
        api.searchPlaces(keyword).enqueue(object : Callback<PlaceSearchResponse> {
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
        val api = JohnRetrofitClient.getClient(requireContext()).create(PlaceSearchAPI::class.java)
        api.sortPlaces(lat, lon).enqueue(object : Callback<PlaceSearchResponse> {
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

    fun updateListByFilter(filter: String, lat: Double, lon: Double) {
        Log.d("BookclubPlaceListFragment", "📌 필터 변경 감지: $filter (lat: $lat, lon: $lon)")

        // 기존 데이터 초기화
        places.clear()
//        adapter.notifyDataSetChanged() // 데이터가 비워졌음을 RecyclerView에 알림

        if (filter == "추천순") {
            fetchPlacesByRecommendation()
        } else {
            fetchPlacesByDistance(lat, lon)
        }
    }

    // 추천순 API 호출 (lat, lon 없이)
    private fun fetchPlacesByRecommendation() {
        val api = JohnRetrofitClient.getClient(requireContext()).create(PlaceSearchAPI::class.java)
        api.sortPlaces(sort = "all").enqueue(object : Callback<PlaceSearchResponse> {
            override fun onResponse(call: Call<PlaceSearchResponse>, response: Response<PlaceSearchResponse>) {
                if (response.isSuccessful) {
                    val newPlaces = response.body()?.result?.places ?: emptyList()
                    Log.d("BookclubPlaceListFragment", "✅ 추천순 검색 결과: ${newPlaces.size}개")

                    places.clear()
                    places.addAll(newPlaces)

                    // ✅ 새로운 Adapter 설정 (강제 RecyclerView 갱신)
//                    requireActivity().runOnUiThread {
//                        adapter = BookclubPlaceRVAdapter(places) { place ->
//                            val fragment = BookclubPlaceDetailFragment().apply {
//                                arguments = Bundle().apply {
//                                    putInt("PLACE_ID", place.placeId)
//                                }
//                            }
//                            (requireActivity() as MainActivity).addFragment(fragment, showBottomNav = false)
//                        }
//                        binding.bookclubPlaceRv.adapter = adapter
//                        adapter.notifyDataSetChanged()
//                    }

                    // ✅ 새로운 Adapter 설정 (RecyclerView 강제 갱신)
                    requireActivity().runOnUiThread {
                        binding.bookclubPlaceRv.adapter = BookclubPlaceRVAdapter(places) { place ->
                            val fragment = BookclubPlaceDetailFragment().apply {
                                arguments = Bundle().apply {
                                    putInt("PLACE_ID", place.placeId)
                                }
                            }
                            (requireActivity() as MainActivity).addFragment(fragment, showBottomNav = false)
                        }
                    }
                } else {
                    Log.e("BookclubPlaceListFragment", "❌ 추천순 응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
                Log.e("BookclubPlaceListFragment", "❌ 추천순 네트워크 오류: ${t.message}")
            }
        })
    }

    // 거리순 API 호출 (lat, lon 사용)
    private fun fetchPlacesByDistance(lat: Double, lon: Double) {
        val api = JohnRetrofitClient.getClient(requireContext()).create(PlaceSearchAPI::class.java)
        api.sortPlaces(sort = "all", lat = lat, lon = lon)
            .enqueue(object : Callback<PlaceSearchResponse> {
                override fun onResponse(call: Call<PlaceSearchResponse>, response: Response<PlaceSearchResponse>) {
                    if (response.isSuccessful) {
                        val newPlaces = response.body()?.result?.places ?: emptyList()
                        Log.d("BookclubPlaceListFragment", "✅ 거리순 검색 결과: ${newPlaces.size}개")

                        places.clear()
                        places.addAll(newPlaces)

                        // ✅ 새로운 Adapter를 설정하여 RecyclerView를 갱신
                        requireActivity().runOnUiThread {
                            binding.bookclubPlaceRv.adapter = BookclubPlaceRVAdapter(places) { place ->
                                val fragment = BookclubPlaceDetailFragment().apply {
                                    arguments = Bundle().apply {
                                        putInt("PLACE_ID", place.placeId) // placeId 전달
                                    }
                                }
                                (requireActivity() as MainActivity).addFragment(fragment, showBottomNav = false)
                            }
                        }
                    } else {
                        Log.e("BookclubPlaceListFragment", "❌ 거리순 응답 실패: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
                    Log.e("BookclubPlaceListFragment", "❌ 거리순 네트워크 오류: ${t.message}")
                }
            })
    }


    // 추천순 API 호출 (lat, lon 없이)
//    private fun fetchPlacesByRecommendation() {
//        RetrofitClient.placeApi.sortPlaces(sort = "all").enqueue(object : Callback<PlaceSearchResponse> {
//            override fun onResponse(call: Call<PlaceSearchResponse>, response: Response<PlaceSearchResponse>) {
//                if (response.isSuccessful) {
//                    val newPlaces = response.body()?.result?.places ?: emptyList()
//                    Log.d("BookclubPlaceListFragment", "✅ 추천순 검색 결과: $newPlaces")
//
//                    places.clear()
//                    places.addAll(newPlaces)
//                    adapter.notifyDataSetChanged()
//                } else {
//                    Log.e("BookclubPlaceListFragment", "❌ 추천순 응답 실패: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
//                Log.e("BookclubPlaceListFragment", "❌ 추천순 네트워크 오류: ${t.message}")
//            }
//        })
//    }

//    private fun fetchPlacesByRecommendation() {
//        RetrofitClient.placeApi.sortPlaces(sort = "all").enqueue(object : Callback<PlaceSearchResponse> {
//            override fun onResponse(call: Call<PlaceSearchResponse>, response: Response<PlaceSearchResponse>) {
//                if (response.isSuccessful) {
//                    val newPlaces = response.body()?.result?.places ?: emptyList()
//                    Log.d("BookclubPlaceListFragment", "✅ 추천순 검색 결과 수신: ${newPlaces.size}개")
//
//                    places.clear()
//                    places.addAll(newPlaces)
//                    adapter.notifyDataSetChanged()
//                    Log.d("BookclubPlaceListFragment", "✅ 추천순 리스트 업데이트 완료")
//                } else {
//                    Log.e("BookclubPlaceListFragment", "❌ 추천순 응답 실패: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
//                Log.e("BookclubPlaceListFragment", "❌ 추천순 네트워크 오류: ${t.message}")
//            }
//        })
//    }


//    // 거리순 API 호출 (lat, lon 사용)
//    private fun fetchPlacesByDistance(lat: Double, lon: Double) {
//        RetrofitClient.placeApi.sortPlaces(sort = "all", lat = lat, lon = lon)
//            .enqueue(object : Callback<PlaceSearchResponse> {
//                override fun onResponse(call: Call<PlaceSearchResponse>, response: Response<PlaceSearchResponse>) {
//                    if (response.isSuccessful) {
//                        val newPlaces = response.body()?.result?.places ?: emptyList()
//                        Log.d("BookclubPlaceListFragment", "✅ 거리순 검색 결과: $newPlaces")
//
//                        places.clear()
//                        places.addAll(newPlaces)
//                        adapter.notifyDataSetChanged()
//                    } else {
//                        Log.e("BookclubPlaceListFragment", "❌ 거리순 응답 실패: ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
//                    Log.e("BookclubPlaceListFragment", "❌ 거리순 네트워크 오류: ${t.message}")
//                }
//            })
//    }
}