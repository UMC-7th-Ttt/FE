package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fe.BookDetail.BookDetail
import com.example.fe.BookDetail.BookDetailActivity
import com.example.fe.JohnRetrofitClient
import com.example.fe.R
import com.example.fe.bookclub_place.BookclubPlaceDetailFragment
import com.example.fe.databinding.ActivityReviewDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ReviewDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReviewDetailBinding
    var reviewId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기존 Intent를 통해 전달된 데이터 가져오기
        reviewId = intent.getLongExtra("reviewId", -1L)
        Log.d("ReviewDetailActivity", "Received reviewId: $reviewId")

        if (reviewId != -1L) {
            fetchReview(reviewId)
        } else {
            Log.e("ReviewDetailActivity", "Invalid reviewId received")
        }

        binding.mypageWriteReviewEditBtn.setOnClickListener {
            val reviewDetailBottomSheet = ReviewDetailBottomSheetFragment()
            reviewDetailBottomSheet.show(supportFragmentManager, reviewDetailBottomSheet.tag)
        }
    }

    private fun fetchReview(reviewId: Long) {
        val api = JohnRetrofitClient.getClient(this).create(ReviewApiService::class.java)
        api.getReviewDetail(reviewId).enqueue(object : Callback<ReviewDetailResponse> {
            override fun onResponse(call: Call<ReviewDetailResponse>, response: Response<ReviewDetailResponse>) {
                if (response.isSuccessful) {
                    val reviewDetailResponse = response.body()
                    reviewDetailResponse?.let {
//                        val bookId = it.result.book.id
//                        val placeId = it.result.place.id

                        val bookId = it.result.book?.id ?: -1
                        val placeId = it.result.place?.id ?: -1

                        binding.dateTv.text = formatDate(it.result.writeDate)

                        binding.reviewBookNameTv.text = it.result.book.title
                        binding.reviewBookAuthorTv.text = it.result.book.author
                        binding.reviewBookStarTv.text = it.result.bookRanking.toString()
                        Glide.with(this@ReviewDetailActivity)
                            .load(it.result.book.cover)
                            .into(binding.reviewBookIv)

                        binding.reviewPlaceNameTv.text = it.result.place.title ?: "알수없음"
                        binding.reviewPlaceLocationTv.text = it.result.place.address
                        binding.reviewPlaceStarTv.text = it.result.placeRanking.toString()
                        Glide.with(this@ReviewDetailActivity)
                            .load(it.result.place.image)
                            .into(binding.reviewPlaceIv)

                        binding.reviewTitleTv.text = it.result.title
                        binding.reviewTv.text = it.result.content

                        if (it.result.isWriter) {
                            binding.mypageWriteReviewEditBtn.visibility = View.VISIBLE
                        } else {
                            binding.mypageWriteReviewEditBtn.visibility = View.GONE
                        }

                        if (it.result.isSecret) {
                            binding.openTv.text = "공개"
                        } else {
                            binding.openTv.text = "비공개"
                        }

                        binding.reviewBookCardview.setOnClickListener {
                            val intent = Intent(binding.root.context, BookDetailActivity::class.java)
                            intent.putExtra("BOOK_ID", bookId.toLong())
                            startActivity(intent)
                        }

                        binding.reviewPlaceCardview.setOnClickListener {
                            openPlaceDetailFragment(placeId)
                        }
                    }
                } else {
                    Log.e("ReviewDetailActivity", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                Log.e("ReviewDetailActivity", "Network Error: ${t.message}")
            }
        })
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    private fun openPlaceDetailFragment(placeId: Int) {
        val fragment = BookclubPlaceDetailFragment.newInstance(placeId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}