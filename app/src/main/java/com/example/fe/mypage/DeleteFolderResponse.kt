package com.example.fe.mypage

data class DeleteFolderResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Int
)