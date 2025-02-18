package com.example.fe.bookclub_book.dataclass

data class BookClubJoinResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val id: Long,
        val bookClubId: Long
    )
}