package com.example.fe.mypage.server

data class DeleteFolderResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Int
)