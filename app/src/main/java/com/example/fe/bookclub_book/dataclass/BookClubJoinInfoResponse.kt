package com.example.fe.bookclub_book.dataclass


data class BookClubJoinInfoResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val bookClubId: Int,
        val startDate: String,
        val endDate: String,
        val recuitNumber: Int,
        val bookInfo: BookInfo
    ) {
        data class BookInfo(
            val id: Int,
            val cover: String,
            val title: String,
            val author: String,
            val category: String,
            val publisher: String,
            val itemPage: Int,
            val description: String,
            val hasEbook: Boolean,
            val isScraped: Boolean
        )
    }
}
