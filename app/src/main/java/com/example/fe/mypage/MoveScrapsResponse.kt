package com.example.fe.mypage

data class MoveScrapsResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)