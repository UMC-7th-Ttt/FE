package com.example.fe.mypage

data class MoveScrapsRequest(
    val newFolderId: Long,
    val scraps: List<Scrap>
) {
    data class Scrap(
        val scrapId: Int,
        val type: String
    )
}