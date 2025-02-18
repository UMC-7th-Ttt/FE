package com.example.fe.mypage

data class ReviewListResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val reviewList: List<Review>,
        val nextCursor: String?,
        val limit: Int,
        val hasNext: Boolean
    ) {
        data class Review(
            val id: Int,
            val cover: String
        )
    }
}

