package com.example.fe.bookclub_book.server

data class BookClubParticipationResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val currentMonth: Int,
        val bookClubs: List<BookClub>
    ) {
        data class BookClub(
            val bookClubId: Int,
            val bookId: Int,
            val bookTitle: String,
            val bookAuthor: String,
            val bookCategory: String,
            val bookCover: String,
            val completionRate: Int
        )
    }
}