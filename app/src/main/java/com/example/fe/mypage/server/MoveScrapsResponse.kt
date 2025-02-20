package com.example.fe.mypage.server

data class MoveScrapsResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)