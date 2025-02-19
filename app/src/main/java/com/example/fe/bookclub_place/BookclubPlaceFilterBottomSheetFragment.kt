package com.example.fe.bookclub_place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fe.databinding.FragmentBookclubPlaceFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookclubPlaceFilterBottomSheetFragment(
    private var selectedFilter: String,  // 현재 선택된 필터 값 (초기 값)
    private val onFilterSelected: (filter: String) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBookclubPlaceFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceFilterBottomSheetBinding.inflate(inflater, container, false)

        // 현재 선택된 필터에 따라 라디오 버튼 기본값 설정
        when (selectedFilter) {
            "추천순" -> binding.filterRecommendationRb.isChecked = true
            else -> binding.filterDistanceRb.isChecked = true // 기본값: 거리순
        }

        // 라디오 버튼 클릭 이벤트 처리
        binding.filterRecommendationRb.setOnClickListener {
            if (!binding.filterRecommendationRb.isChecked) return@setOnClickListener
            selectedFilter = "추천순"
            onFilterSelected(selectedFilter)
            dismiss()
        }

        binding.filterDistanceRb.setOnClickListener {
            if (!binding.filterDistanceRb.isChecked) return@setOnClickListener
            selectedFilter = "거리순"
            onFilterSelected(selectedFilter)
            dismiss()
        }

        return binding.root
    }
}
