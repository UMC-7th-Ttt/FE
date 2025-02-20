package com.example.fe.Review

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReviewApiService {
    @POST("/api/reviews/")
    fun submitReview(@Body reviewRequest: ReviewRequest): Call<ReviewResponse>
}