package com.example.fe.bookclub_book.dataclass

data class BookClubReviewDetailResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val id: Int,
        val title: String,
        val content: String,
        val currentPage: Int,
        val imgUrl: String,
        val isSecret: Boolean,
        val memberInfo: MemberInfo
    ) {
        data class MemberInfo(
            val id: Int,
            val nickname: String,
            val profileUrl: String
        )
    }
}
