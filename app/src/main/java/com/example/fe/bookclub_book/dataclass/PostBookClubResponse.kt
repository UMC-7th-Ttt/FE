package com.example.fe.bookclub_book.dataclass

data class PostJoinBookClubResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PostJoinBookClubResult
)

data class PostJoinBookClubResult(
    val id: Int,
    val bookClubId: Int
)