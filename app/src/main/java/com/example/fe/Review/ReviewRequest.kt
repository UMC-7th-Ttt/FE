package com.example.fe.Review

data class ReviewRequest(
    val title: String,
    val content: String,
    val bookRanking: Int,
    val placeRanking: Int,
    val isSecret: Boolean,
    val writeDate: String,
    val bookId: Int?,
    val placeId: Int?
)

data class ReviewResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ReviewResult?
)

data class ReviewResult(
    val reviewId: Int
)
