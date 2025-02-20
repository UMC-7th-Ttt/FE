package com.example.fe.mypage.server

data class MoveScrapsRequest(
    val newFolderId: Long,
    val scraps: List<Scrap>
) {
    data class Scrap(
        val scrapId: Int,
        val type: String
    )
}