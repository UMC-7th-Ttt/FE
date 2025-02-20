package com.example.fe.mypage.server

data class CreateFolderResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: FolderResult
)

data class FolderResult(
    val folderId: Int,
    val name: String,
    val images: List<String>
)