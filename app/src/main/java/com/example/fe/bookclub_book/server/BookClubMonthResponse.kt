package com.example.fe.bookclub_book.server

data class BookClubMonthResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val bookClubPreViewList: List<BookClubPreview>,
        val totalPage: Int,
        val listSize: Int,
        val totalElements: Int,
        val isFirstPage: Boolean,
        val isLastPage: Boolean
    ) {
        data class BookClubPreview(
            val bookClubId: Int,
            val title: String
        )
    }
}