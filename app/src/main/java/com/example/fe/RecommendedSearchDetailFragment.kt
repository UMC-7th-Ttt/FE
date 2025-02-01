package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.databinding.FragmentRecommendedSearchDetailBinding

class RecommendedSearchDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecommendedSearchDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedSearchDetailBinding.inflate(inflater, container, false)

        // 전달받은 카테고리 제목 적용
        val categoryTitle = arguments?.getString("CATEGORY_TITLE", "추천 검색") ?: "추천 검색"
        binding.recommendedSearchDetailTitleTv.text = categoryTitle

        initPopularBookListRV()
        initRecommendedBookListRV()
        initEditorPickBookListRV()

        return binding.root
    }

    private fun initPopularBookListRV() {
        val bookList = listOf(
            Pair(R.drawable.img_book_cover1, false),
            Pair(R.drawable.img_book_cover2, true),
            Pair(R.drawable.img_book_cover3, false),
            Pair(R.drawable.img_book_cover4, true),
        )

        val adapter = PopularBookListRVAdapter(bookList)
        binding.popularBookListRv.adapter = adapter
        binding.popularBookListRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initRecommendedBookListRV() {
        val bookList = listOf(
            Book(R.drawable.img_book_cover7, "소설 보다 가을", "한강 시집", "",true),
            Book(R.drawable.img_book_cover6, "모순", "한강 시집", "", false),
            Book(R.drawable.img_book_cover5, "홍학의 자리", "정혜연", "", true),
            Book(R.drawable.img_book_cover1, "분명 좋은 일만 생길 거예요", "김선현", "", false),
            Book(R.drawable.img_book_cover3, "작별인사", "김영하", "", false)
        )

        val bookAdapter = RecommendedBookListRVAdapter(bookList)
        binding.recommendedBookListRv.adapter = bookAdapter
        binding.recommendedBookListRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initEditorPickBookListRV() {
        val editorPickBooks = listOf(
            Book(R.drawable.img_book_cover1, "분명 좋은 일만 생길 거예요", "이슬비 에세이", "📙 \"북레터 삶에 대한 고찰\"", true),
            Book(R.drawable.img_book_cover2, "힘들어? 그래도 해야지 어떡해", "아찔", "📙 \"북레터 삶에 대한 고찰\"", false),
            Book(R.drawable.img_book_cover5, "홍학의 자리", "한강", "📙 \"북레터 삶에 대한 고찰\"", true),
            Book(R.drawable.img_book_cover4, "사랑은 모든걸 아니까요", "정홍수", "📙 \"북레터 삶에 대한 고찰\"", false)
        )

        val editorPickAdapter = EditorPickBookListRVAdapter(editorPickBooks)
        binding.editorPickBookListRv.adapter = editorPickAdapter
        binding.editorPickBookListRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
