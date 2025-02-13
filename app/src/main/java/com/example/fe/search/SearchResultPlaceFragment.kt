package com.example.fe.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.bookclub_place.BookclubPlaceDetailFragment
import com.example.fe.bookclub_place.api.PlaceResponse
import com.example.fe.bookclub_place.api.PlaceSearchResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentSearchResultPlaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultPlaceFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultPlaceBinding
    private lateinit var keyword: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultPlaceBinding.inflate(inflater, container, false)
        keyword = arguments?.getString("KEYWORD") ?: ""

        searchPlaces(keyword)

        return binding.root
    }

    private fun searchPlaces(keyword: String) {
        RetrofitClient.placeApi.searchPlaces(keyword).enqueue(object : Callback<PlaceSearchResponse> {
            override fun onResponse(call: Call<PlaceSearchResponse>, response: Response<PlaceSearchResponse>) {
                if (response.isSuccessful) {
                    val placeList = response.body()?.result?.places ?: emptyList()
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


            // 이미지 클릭 시 detail fragment로 이동
            val adapter = SearchResultPlaceRVAdapter(places) { place ->
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
