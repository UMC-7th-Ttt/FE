package com.example.fe.bookclub_book.dataclass

data class CalendarResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val reviewList: List<Review>
    ) {
        data class Review(
            val id: Int,
            val cover: String,
            val writeDate: String
        )
    }
}