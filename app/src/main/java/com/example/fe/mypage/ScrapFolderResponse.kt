package com.example.fe.mypage

data class ScrapFolderResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val folderCount: Int,
        val folders: List<Folder>
    ) {
        data class Folder(
            val folderId: Int,
            val name: String,
            val images: List<String>
        )
    }
}