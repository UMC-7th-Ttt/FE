package com.example.fe.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.JohnRetrofitClient
import com.example.fe.R
import com.example.fe.bookclub_place.BookclubPlaceDetailFragment
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.bookclub_place.api.PlaceSearchResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentSearchResultPlaceBinding
import com.example.fe.Review.SpaceReviewActivity
import com.example.fe.bookclub_place.api.PlaceSearchAPI
import com.example.fe.search.api.BookSearchAPI
import com.example.fe.search.api.BookSuggestionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultPlaceFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultPlaceBinding
    private lateinit var keyword: String
    private var callerActivity: String? = null // 호출한 액티비티 정보

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultPlaceBinding.inflate(inflater, container, false)
        keyword = arguments?.getString("KEYWORD") ?: ""
        callerActivity = activity?.intent?.getStringExtra("CALLER") // SearchMainActivity에서 받은 값

        searchPlaces(keyword)

        return binding.root
    }

    private fun searchPlaces(keyword: String) {
        val api = JohnRetrofitClient.getClient(requireContext()).create(PlaceSearchAPI::class.java)
        api.searchPlaces(keyword)
            .enqueue(object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    call: Call<PlaceSearchResponse>,
                    response: Response<PlaceSearchResponse>


                ) {
                    if (response.isSuccessful) {
                        val placeList = response.body()?.result?.places ?: emptyList()

                        // 🔥 전체 API 응답 로그 추가
                        Log.d("API_RESPONSE", "📡 전체 응답: ${response.body()}")

                        // 🔥 개별 place 데이터 확인
                        placeList.forEach { place ->
                            Log.d("API_RESPONSE", "📌 PLACE_ID: ${place.placeId}")
                            Log.d("API_RESPONSE", "📌 PLACE_TITLE: ${place.title}")
                            Log.d("API_RESPONSE", "📌 PLACE_IMAGE: ${place.image}") // 🚨 여기 확인!
                        }
                        displaySearchResults(placeList) // 검색 결과 표시
                    } else {
                        Log.e("API_ERROR", "❌ ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
                    Log.e("NETWORK_ERROR", "❌ ${t.localizedMessage}")
                }
            })
    }

    private fun displaySearchResults(places: List<PlaceResponse>) {
        if (places.isEmpty()) {
            binding.searchResultPlaceRv.visibility = View.GONE
            binding.emptyResultTv.visibility = View.VISIBLE
        } else {
            binding.searchResultPlaceRv.visibility = View.VISIBLE
            binding.emptyResultTv.visibility = View.GONE

            val adapter = SearchResultPlaceRVAdapter(requireContext(), places) { place ->
                val fragment = BookclubPlaceDetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt("PLACE_ID", place.placeId) // placeId 전달
                    }
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_layout, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            binding.searchResultPlaceRv.layoutManager = LinearLayoutManager(requireContext())
            binding.searchResultPlaceRv.adapter = adapter
        }
    }
}
