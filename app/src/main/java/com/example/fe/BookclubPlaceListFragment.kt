package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.R
import com.example.fe.BookclubPlaceRVAdapter
import com.example.fe.databinding.FragmentPlaceListBinding
import com.example.fe.Place

class PlaceListFragment : Fragment() {

    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data
        val places = listOf(
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1),
            Place("서울책보고", "서점", 4.8, R.drawable.img_place2),
            Place("카페꼼마 합정점", "카페", 4.8, R.drawable.img_place3),
            Place("전부책방스튜디오", "서점", 4.8, R.drawable.img_place4),
            Place("알키미스타", "카페", 4.8, R.drawable.img_place5)
        )

        // RecyclerView 초기화
        val adapter = BookclubPlaceRVAdapter(places)
        binding.bookclubPlaceRv.layoutManager = LinearLayoutManager(requireContext())
        binding.bookclubPlaceRv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
