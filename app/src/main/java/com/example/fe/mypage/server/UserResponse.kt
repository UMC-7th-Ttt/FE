package com.example.fe.mypage.server

data class UserResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val createdAt: String,
        val id: Int,
        val nickname: String,
        val profileUrl: String,
        val role: String
    )
}

