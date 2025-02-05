

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
    private val places = mutableListOf<PlaceResponse>() // API에서 받아온 데이터를 저장할 리스트
    private var keyword: String = "검색 결과" // 기본 키워드

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceListBinding.inflate(inflater, container, false)

        // `parentFragment`를 통해 `BookclubPlaceFragment`의 `bookclubPlaceTitleTv` 값 가져오기
//        val parentFragment = parentFragment as? BookclubPlaceFragment
//        keyword = parentFragment?.getKeywordFromTitleTv() ?: "검색 결과"
//        Log.d("BookclubPlaceListFragment", "✅ getKeywordFromTitleTv로 받은 키워드: $keyword")

        keyword = arguments?.getString("KEYWORD", "검색 결과") ?: "검색 결과"
        Log.d("BookclubPlaceListFragment", "✅ arguments로 받은 키워드: $keyword")


        // RecyclerView 초기화
        initBookclubPlaceListRV()

        // API 요청 실행 (키워드 기반 장소 검색)
        searchPlaces(keyword)

        return binding.root
    }

    // RecyclerView 초기화
    private fun initBookclubPlaceListRV() {
        adapter = BookclubPlaceRVAdapter(places) { place ->
            (requireActivity() as MainActivity).addFragment(
                BookclubPlaceDetailFragment(),
                showBottomNav = false,
            )
        }
        binding.bookclubPlaceRv.layoutManager = LinearLayoutManager(requireContext())
        binding.bookclubPlaceRv.adapter = adapter
    }

    // API를 호출하여 장소 데이터를 가져오는 함수
    private fun searchPlaces(keyword: String) {
        RetrofitClient.api.searchPlaces(keyword).enqueue(object : Callback<PlaceSearchResponse> {
            override fun onResponse(
                call: Call<PlaceSearchResponse>,
                response: Response<PlaceSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val newPlaces = response.body()?.result?.places ?: emptyList()
                    Log.d("BookclubPlaceListFragment", "✅ 검색 결과: $newPlaces")

                    places.clear() // 기존 데이터 삭제
                    places.addAll(newPlaces) // 새로운 데이터 추가
                    adapter.notifyDataSetChanged() // RecyclerView 업데이트
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

