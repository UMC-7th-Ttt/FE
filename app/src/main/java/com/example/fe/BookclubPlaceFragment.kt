package com.example.fe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentBookclubPlaceBinding

class BookclubPlaceFragment : Fragment() {

    private lateinit var binding: FragmentBookclubPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceBinding.inflate(inflater, container, false)

        // 기본 프래그먼트 설정 (BookclubPlaceListFragment 초기 화면)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.bookclub_place_list_frm, BookclubPlaceListFragment())
            .commit()

        // 공통 클릭 리스너 설정 (BookclubPlaceSearchFragment로 이동)
        val commonClickListener = View.OnClickListener {
            val intent = Intent(requireContext(), BookclubPlaceSearchActivity::class.java)
            startActivity(intent)
        }

        binding.bookclubPlaceTitleTv.setOnClickListener(commonClickListener)
        binding.bookclubPlaceArrowDownIc.setOnClickListener(commonClickListener)

        binding.bookclubPlaceSearchIc.setOnClickListener {
            val intent = Intent(requireContext(), SearchMainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}
