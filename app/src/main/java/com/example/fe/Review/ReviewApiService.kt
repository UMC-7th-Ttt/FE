package com.example.fe.Review

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApiService {
    @POST("/api/reviews/")
    fun submitReview(@Body reviewRequest: ReviewRequest): Call<ReviewResponse>

    @GET("/api/reviews/{reviewId}")
    fun getReviewDetail(
        @Path("reviewId") reviewId: Long
    ): Call<ReviewDetailResponse>

    @DELETE("/api/reviews/{reviewId}")
    fun deleteReview(@Path("reviewId") reviewId: Long): Call<DeleteReviewResponse>
}