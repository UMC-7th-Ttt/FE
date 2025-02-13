package com.example.fe.bookclub_place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.fe.R
import com.example.fe.databinding.FragmentBookclubPlaceFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookclubPlaceFilterBottomSheetFragment(
    private val onFilterSelected: (filter: String) -> Unit,
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBookclubPlaceFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubPlaceFilterBottomSheetBinding.inflate(inflater, container, false)

        // 라디오 버튼 클릭 이벤트 처리
        binding.filterRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedFilter = binding.root.findViewById<RadioButton>(checkedId).text.toString()
            onFilterSelected(selectedFilter)
            dismiss() // 바텀시트 닫기
        }

        return binding.root
    }
}
