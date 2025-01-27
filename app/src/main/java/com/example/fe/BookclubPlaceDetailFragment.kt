package com.example.fe

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.fe.R
import com.example.fe.databinding.FragmentBookclubPlaceDetailBinding

class BookclubPlaceDetailFragment : DialogFragment() {

    private lateinit var binding: FragmentBookclubPlaceDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceDetailBinding.inflate(inflater, container, false)

        // Fullscreen 설정
        requireActivity().window.apply {
            statusBarColor = Color.TRANSPARENT // 상태바를 투명하게
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 상태바 위로 레이아웃 확장
                    )
        }

        // 북마크 아이콘 클릭 리스너
        binding.bookclubPlaceDetailBookmarkIv.setOnClickListener {
            val scrapBottomSheet = ScrapBottomSheetFragment()
            scrapBottomSheet.show(parentFragmentManager, scrapBottomSheet.tag)
        }

        return binding.root
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

        // 상태바 복원
        requireActivity().window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
        }
    }
}

