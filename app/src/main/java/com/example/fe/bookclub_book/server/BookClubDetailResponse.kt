package com.example.fe.bookclub_book.server

data class BookClubDetailResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val bookClubId: Int,
        val elapsedWeeks: Int,
        val myCompletionRate: Int,
        val recommendedCompletionRate: Int,
        val bookInfo: BookInfo,
        val members: List<Member>
    ) {
        data class BookInfo(
            val id: Int,
            val cover: String,
            val title: String,
            val author: String,
            val category: String,
            val publisher: String,
            val isScraped: Boolean,
            val itemLink: String
        )

        data class Member(
            val id: Int,
            val nickname: String,
            val profileUrl: String,
            val hasReviewed: Boolean
        )
    }
}
