package com.example.fe.bookclub_book.dataclass

data class BookClubUserResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val memberId: Int,
        val profileUrl: String
    )
}