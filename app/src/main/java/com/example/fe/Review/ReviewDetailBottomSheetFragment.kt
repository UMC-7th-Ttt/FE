package com.example.fe.Review

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fe.JohnRetrofitClient
import com.example.fe.bookclub_book.server.BookClubRetrofitInterface
import com.example.fe.databinding.FragmentMypageScrapDetailBottomSheetBinding
import com.example.fe.databinding.FragmentReviewDetailBottomSheetBinding
import com.example.fe.mypage.adapter.MyPageScrapDetailBottomSheetRVAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewDetailBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentReviewDetailBottomSheetBinding
    private var reviewId: Long = 0L
    private var reviewDeletedListener: ReviewDeletedListener? = null

    interface ReviewDeletedListener {
        fun onReviewDeleted()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReviewDeletedListener) {
            reviewDeletedListener = context
        } else if (parentFragment is ReviewDeletedListener) {
            reviewDeletedListener = parentFragment as ReviewDeletedListener
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewDetailBottomSheetBinding.inflate(inflater, container, false)

        reviewId = (activity as? ReviewDetailActivity)?.reviewId ?: -1L

        if (reviewId == -1L) {
            Toast.makeText(context, "Invalid review ID", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.deleteTv.setOnClickListener {
            deleteReview(reviewId)
        }

        binding.cancelTv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun deleteReview(reviewId: Long) {
        val api = JohnRetrofitClient.getClient(requireContext()).create(ReviewApiService::class.java)
        api.deleteReview(reviewId).enqueue(object : Callback<DeleteReviewResponse> {
            override fun onResponse(call: Call<DeleteReviewResponse>, response: Response<DeleteReviewResponse>) {
                if (response.isSuccessful) {
                    val deleteResponse = response.body()
                    deleteResponse?.let {
                        if (it.isSuccess) {
                            Toast.makeText(requireContext(), "Review deleted successfully", Toast.LENGTH_SHORT).show()
                            reviewDeletedListener?.onReviewDeleted()
                            dismiss()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete review: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("ReviewDetailActivity", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DeleteReviewResponse>, t: Throwable) {
                Log.e("ReviewDetailActivity", "Network Error: ${t.message}")
            }
        })
    }
}