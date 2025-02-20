package com.example.fe.Review

data class ReviewRequest(
    val title: String,
    val content: String,
    val bookRanking: Float,
    val placeRanking: Float,
    val isSecret: Boolean,
    val writeDate: String,
    val bookId: Long?,
    val placeId: Long?
)

data class ReviewResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ReviewResult?
)

data class ReviewResult(
    val reviewId: Long
)
