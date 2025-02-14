package com.example.fe.bookclub_book.server

data class BookClubByMonthResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val currentMonth: Int,
        val bookClubs: List<BookClub>,
        val nextCursorTitle: String?,
        val limit: Int,
        val hasNext: Boolean
    ) {
        data class BookClub(
            val bookClubId: Int,
            val bookId: Int,
            val bookTitle: String,
            val author: String,
            val bookCover: String,
            val bookCategory: String
        )
    }
}