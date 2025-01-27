package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.databinding.FragmentBookclubPlaceListBinding

class BookclubPlaceListFragment : Fragment() {

    private lateinit var binding: FragmentBookclubPlaceListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceListBinding.inflate(inflater, container, false)

        initBookclubPlaceListRV()

        return binding.root
    }

    private fun initBookclubPlaceListRV() {
        val places = listOf(
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1, true),
            Place("서울책보고", "서점", 4.8, R.drawable.img_place2, false),
            Place("카페꼼마 합정점", "카페", 4.8, R.drawable.img_place3, true),
            Place("전부책방스튜디오", "서점", 4.8, R.drawable.img_place4, false),
            Place("알키미스타", "카페", 4.8, R.drawable.img_place5, false)
        )

        val adapter = BookclubPlaceRVAdapter(places)
        binding.bookclubPlaceRv.layoutManager = LinearLayoutManager(requireContext())
        binding.bookclubPlaceRv.adapter = adapter
    }
}
