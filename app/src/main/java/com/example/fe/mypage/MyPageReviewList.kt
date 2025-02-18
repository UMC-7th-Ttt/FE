package com.example.fe.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fe.Review.ReviewActivity
import com.example.fe.databinding.ActivityMypageReviewListBinding
import com.example.fe.mypage.adapter.MyPageReviewReviewRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.fe.mypage.api

class MyPageReviewList : AppCompatActivity() {
    private lateinit var binding: ActivityMypageReviewListBinding
    private lateinit var myPageReviewReviewRVAdapter: MyPageReviewReviewRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageReviewListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.mypageWriteReviewFloatingBtn.setOnClickListener {
            this.let { ctx ->
                val intent = Intent(ctx, ReviewActivity::class.java)
                startActivity(intent)
            }
        }

        initRecyclerView()
        loadReviews(0, 10)
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        binding.mypageReviewRv.layoutManager = layoutManager

        myPageReviewReviewRVAdapter = MyPageReviewReviewRVAdapter(object : MyPageReviewReviewRVAdapter.MyItemClickListener {
            override fun onItemClick(review: ReviewListResponse.Result.Review) {
                // 아이템 클릭 시 처리 로직
                Toast.makeText(this@MyPageReviewList, "Clicked: ${review.id}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.mypageReviewRv.adapter = myPageReviewReviewRVAdapter
    }

    private fun loadReviews(cursor: Int, limit: Int) {
        Log.d("MyPageReviewList", "Loading reviews with cursor: $cursor and limit: $limit")
        api.getReviews(cursor, limit).enqueue(object : Callback<ReviewListResponse> {
            override fun onResponse(call: Call<ReviewListResponse>, response: Response<ReviewListResponse>) {
                if (response.isSuccessful) {
                    val reviewListResponse = response.body()
                    reviewListResponse?.let {
                        if (it.isSuccess) {
                            myPageReviewReviewRVAdapter.setReview(it.result.reviewList)
                        } else {
                            Log.e("MyPageReviewList", "Failed to load reviews: ${it.message}")
                        }
                    }
                } else {
                    Log.e("MyPageReviewList", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MyPageReviewList, "Failed to load reviews: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                Log.e("MyPageReviewList", "Network Error: ${t.message}")
                Toast.makeText(this@MyPageReviewList, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}