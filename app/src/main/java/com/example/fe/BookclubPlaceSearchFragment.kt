package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.FragmentBookclubPlaceSearchBinding

class BookclubPlaceSearchFragment : DialogFragment() {

    private lateinit var binding: FragmentBookclubPlaceSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceSearchBinding.inflate(inflater, container, false)

        initBookclubPlaceRecentSearchRV()
        initBookclubRecommendedPlaceRV()

        return binding.root
    }

    private fun initBookclubPlaceRecentSearchRV() {
        val recentSearches = listOf("북카페", "공간 대여", "커피", "서점", "화장실", "성북구", "디저트")

        val adapter = RecentSearchRVAdapter(recentSearches)
        binding.bookclubPlaceRecentSearchListRv.adapter = adapter
        binding.bookclubPlaceRecentSearchListRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initBookclubRecommendedPlaceRV() {
        val recommendedPlaces = listOf(
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1, false),
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1, false),
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1, false),
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1, false),
            Place("서울책보고", "서점", 4.5, R.drawable.img_place2, false),
            Place("카페꼼마 합정점", "카페", 4.7, R.drawable.img_place3, false),
            Place("전부책방스튜디오", "서점", 4.3, R.drawable.img_place4, false),
            Place("알키미스타", "카페", 4.6, R.drawable.img_place5, false)
        )

        val recommendedPlaceAdapter = BookclubRecommendedPlaceRVAdapter(recommendedPlaces)
        binding.bookclubRecommendedPlaceListRv.adapter = recommendedPlaceAdapter
        binding.bookclubRecommendedPlaceListRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // 액티비티의 상단 뷰 숨기기
        val activity = activity as BookclubPlaceActivity
        activity.hideTopViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 액티비티의 상단 뷰 다시 보이기
        val activity = activity as BookclubPlaceActivity
        activity.showTopViews()
    }
}
