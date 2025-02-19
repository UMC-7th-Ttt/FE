package com.example.fe.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.R
import com.example.fe.bookclub_place.BookclubPlaceDetailFragment
import com.example.fe.bookclub_place.api.PlaceEditorPickResponse
import com.example.fe.bookclub_place.api.PlaceSuggestionResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentRecommendedSearchPlaceDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendedSearchPlaceDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecommendedSearchPlaceDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedSearchPlaceDetailBinding.inflate(inflater, container, false)

        initBackBtnClickListener()
        initRecommendedPlaceListRV()
        initEditorPickPlaceListRV()

        return binding.root
    }

    private fun initBackBtnClickListener() {
        binding.recommendedSearchPlaceDetailBackIv.setOnClickListener {
            parentFragmentManager.popBackStack() // 뒤로 가기
        }
    }

    private fun initRecommendedPlaceListRV() {
        RetrofitClient.placeApi.getPlaceSuggestions()
            .enqueue(object : Callback<PlaceSuggestionResponse> {
                override fun onResponse(
                    call: Call<PlaceSuggestionResponse>,
                    response: Response<PlaceSuggestionResponse>
                ) {
                    if (response.isSuccessful) {
                        val placeList = response.body()?.result?.places ?: emptyList()

                        // 공간 detail fragment로 이동
                        val placeAdapter = RecommendedPlaceListRVAdapter(placeList) { place ->
                            val fragment = BookclubPlaceDetailFragment().apply {
                                arguments = Bundle().apply {
                                    putInt("PLACE_ID", place.placeId)
                                }
                            }
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.main_recommended_search_place_layout, fragment)
                                .addToBackStack(null)
                                .commit()
                        }

                        binding.recommendedPlaceListRv.adapter = placeAdapter
                        binding.recommendedPlaceListRv.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    } else {
                        Log.e("API Error", "❌ ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PlaceSuggestionResponse>, t: Throwable) {
                    Log.e("Network Error", "❌ ${t.message}")
                }
            })
    }

    private fun initEditorPickPlaceListRV() {
        RetrofitClient.placeApi.getEditorPickPlaces()
            .enqueue(object : Callback<PlaceEditorPickResponse> {
                override fun onResponse(
                    call: Call<PlaceEditorPickResponse>,
                    response: Response<PlaceEditorPickResponse>
                ) {
                    if (response.isSuccessful) {
                        val placeList = response.body()?.result?.places ?: emptyList()
//                        val placeAdapter = EditorPickPlaceListRVAdapter(placeList)

                        // 클릭 시 상세 페이지 이동하도록 수정
                        val placeAdapter = EditorPickPlaceListRVAdapter(placeList) { place ->
                            val fragment = BookclubPlaceDetailFragment().apply {
                                arguments = Bundle().apply {
                                    putInt("PLACE_ID", place.placeId) // 장소 ID 전달
                                }
                            }

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.main_recommended_search_place_layout, fragment)
                                .addToBackStack(null)
                                .commit()
                        }

                        binding.editorPickPlaceListRv.adapter = placeAdapter
                        binding.editorPickPlaceListRv.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    } else {
                        Log.e("API Error", "❌ ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PlaceEditorPickResponse>, t: Throwable) {
                    Log.e("Network Error", "❌ ${t.message}")
                }
            })
    }

}
