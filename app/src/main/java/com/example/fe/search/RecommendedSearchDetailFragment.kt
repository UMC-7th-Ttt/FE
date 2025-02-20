package com.example.fe.search

import HomeFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.JohnRetrofitClient
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.FragmentRecommendedSearchDetailBinding
import com.example.fe.search.api.BookSuggestionResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.scrap.api.ScrapAPI
import com.example.fe.search.api.BookEditorPickResponse
import com.example.fe.search.api.BookSearchAPI
import com.example.fe.search.api.BookUserSuggestionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendedSearchDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecommendedSearchDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedSearchDetailBinding.inflate(inflater, container, false)

        val categoryTitle = arguments?.getString("CATEGORY_TITLE", "추천 검색") ?: "추천 검색"
        binding.recommendedSearchDetailTitleTv.text = categoryTitle


        initBackBtnClickListener()
        initHomeBtnClickListener()
        initPopularBookListRV(categoryTitle)
        initRecommendedBookListRV()
        initEditorPickBookListRV()

        return binding.root
    }

    private fun initBackBtnClickListener() {
        binding.recommendedSearchDetailBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun initHomeBtnClickListener() {
        binding.recommendedSearchDetailHomeIv.setOnClickListener {
            // MainActivity로 이동하면서 GO_HOME 플래그를 명확히 설정
            val mainActivityIntent = Intent(requireActivity(), MainActivity::class.java)
            mainActivityIntent.putExtra("GO_HOME", true) // 🚀 홈 이동 플래그 설정

            startActivity(mainActivityIntent)

            // SearchMainActivity 종료
            requireActivity().finish()
        }
    }

    private fun initPopularBookListRV(categoryTitle: String) {
        val categoryName = when (categoryTitle) {
            "한국 문학" -> "koreanLiterature"
            "인문" -> "humanities"
            "자기 계발" -> "selfDevelopment"
            "에세이/여행" -> "essayAndTravel"
            "사회/자연 과학" -> "socialAndNaturalSciences"
            "세계 문학" -> "worldLiterature"
            else -> "koreanLiterature"
        }

        val api = JohnRetrofitClient.getClient(requireContext()).create(BookSearchAPI::class.java)
        api.getBookSuggestions(categoryName).enqueue(object : Callback<BookSuggestionResponse> {
            override fun onResponse(call: Call<BookSuggestionResponse>, response: Response<BookSuggestionResponse>) {
                if (response.isSuccessful) {
                    val bookList = response.body()?.result?.books ?: emptyList()
                    val bookAdapter = PopularBookListRVAdapter(requireContext(), bookList)
                    binding.popularBookListRv.adapter = bookAdapter
                    binding.popularBookListRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                } else {
                    Log.e("API Error", "${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BookSuggestionResponse>, t: Throwable) {
                Log.e("Network Error", "${t.message}")
            }
        })
    }

    private fun initRecommendedBookListRV() {
        val api = JohnRetrofitClient.getClient(requireContext()).create(BookSearchAPI::class.java)
        api.getUserBookSuggestions()
            .enqueue(object : Callback<BookUserSuggestionResponse> {
                override fun onResponse(call: Call<BookUserSuggestionResponse>, response: Response<BookUserSuggestionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.result

                        val memberNickname = result?.memberNickname ?: "사용자"

                        // memberNickname을 recommended_book_intro_tv에 적용
                        binding.recommendedBookIntroTv.text = "${memberNickname}님의 맞춤 추천 독서를 즐겨봐요!"


                        val bookList = result?.books ?: emptyList()
                        val bookAdapter = RecommendedBookListRVAdapter(requireContext(), bookList)
                        binding.recommendedBookListRv.adapter = bookAdapter
                        binding.recommendedBookListRv.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    } else {
                        Log.e("API Error", "❌ ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<BookUserSuggestionResponse>, t: Throwable) {
                    Log.e("Network Error", "❌ ${t.message}")
                }
            })
    }

    private fun initEditorPickBookListRV() {
        val api = JohnRetrofitClient.getClient(requireContext()).create(BookSearchAPI::class.java)
        api.getEditorPickBooks()
            .enqueue(object : Callback<BookEditorPickResponse> {
                override fun onResponse(call: Call<BookEditorPickResponse>, response: Response<BookEditorPickResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.result

                        val bookLetterTitle = result?.bookLetterTitle ?: "북레터 한 줄"

                        val bookList = result?.books ?: emptyList()
                        val bookAdapter = EditorPickBookListRVAdapter(requireContext(), bookList, bookLetterTitle)
                        binding.editorPickBookListRv.adapter = bookAdapter
                        binding.editorPickBookListRv.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    } else {
                        Log.e("API Error", "❌ ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<BookEditorPickResponse>, t: Throwable) {
                    Log.e("Network Error", "❌ ${t.message}")
                }
            })
    }
}