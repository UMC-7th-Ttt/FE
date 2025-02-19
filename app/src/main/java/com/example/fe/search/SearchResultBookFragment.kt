package com.example.fe.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentSearchResultBookBinding
import com.example.fe.search.api.BookResponse
import com.example.fe.search.api.BookSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultBookFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBookBinding
    private lateinit var keyword: String
    private var callerActivity: String? = null // 호출한 액티비티 정보

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBookBinding.inflate(inflater, container, false)
        keyword = arguments?.getString("KEYWORD") ?: ""
        callerActivity = activity?.intent?.getStringExtra("CALLER") // SearchMainActivity에서 받은 값

        searchBooks(keyword)

        return binding.root
    }

    private fun searchBooks(keyword: String) {
        RetrofitClient.bookApi.searchBooks(keyword).enqueue(object : Callback<BookSearchResponse> {
            override fun onResponse(call: Call<BookSearchResponse>, response: Response<BookSearchResponse>) {
                if (response.isSuccessful) {
                    val bookList = response.body()?.result?.books ?: emptyList()
                    displaySearchResults(bookList)
                } else {
                    Log.e("API_ERROR", "❌ ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BookSearchResponse>, t: Throwable) {
                Log.e("NETWORK_ERROR", "❌ ${t.localizedMessage}")
            }
        })
    }

    private fun displaySearchResults(books: List<BookResponse>) {
        if (books.isEmpty()) {
            binding.searchResultBookRv.visibility = View.GONE
            binding.emptyResultTv.visibility = View.VISIBLE
        } else {
            binding.searchResultBookRv.visibility = View.VISIBLE
            binding.emptyResultTv.visibility = View.GONE

            val adapter = SearchResultBookRVAdapter(books, callerActivity)
            binding.searchResultBookRv.layoutManager = LinearLayoutManager(requireContext())
            binding.searchResultBookRv.adapter = adapter
        }
    }

}
