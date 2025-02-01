package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.databinding.FragmentRecommendedSearchPlaceDetailBinding

class RecommendedSearchPlaceDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecommendedSearchPlaceDetailBinding
    private lateinit var recommendedPlaceAdapter: RecommendedPlaceListRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedSearchPlaceDetailBinding.inflate(inflater, container, false)

        initRecommendedPlaceListRV()
        initEditorPickPlaceListRV()

        return binding.root
    }

    private fun initRecommendedPlaceListRV() {
        val recommendedPlaces = listOf(
            Place("카페꼼마 합정점", "카페", 4.7, R.drawable.img_place5, true),
            Place("인덱스숍", "카페", 4.8, R.drawable.img_place1, false),
            Place("서울책보고", "서점", 4.5, R.drawable.img_place2, true),
            Place("전부책방스튜디오", "서점", 4.3, R.drawable.img_place4, false),
            Place("알키미스타", "카페", 4.6, R.drawable.img_place5, false)
        )

        recommendedPlaceAdapter = RecommendedPlaceListRVAdapter(recommendedPlaces)
        binding.recommendedPlaceListRv.apply {
            adapter = recommendedPlaceAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initEditorPickPlaceListRV() {
        val editorPickBooks = listOf(
            Book(R.drawable.img_book_cover1, "동경책방", "독립서점", "📙 MZ들이 사랑하는 독립서점", true),
            Book(R.drawable.img_book_cover2, "유어마인드", "북카페", "☕️ 야외에서도 읽을 수 있는 북카페", false),
            Book(R.drawable.img_book_cover5, "쏘블루", "독립서점", "📙 MZ들이 사랑하는 독립서점", true),
            Book(R.drawable.img_book_cover4, "갑을문고", "북카페", "☕️ 야외에서도 읽을 수 있는 북카페", false)
        )

        val editorPickAdapter = EditorPickBookListRVAdapter(editorPickBooks)
        binding.editorPickPlaceListRv.adapter = editorPickAdapter
        binding.editorPickPlaceListRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
