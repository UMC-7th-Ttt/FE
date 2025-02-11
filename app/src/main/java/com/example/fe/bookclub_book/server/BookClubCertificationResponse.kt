package com.example.fe.bookclub_book.server

data class BookClubCertificationResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val id: Int
    )
}