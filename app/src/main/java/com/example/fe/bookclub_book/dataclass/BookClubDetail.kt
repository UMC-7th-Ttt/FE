package com.example.fe.bookclub_book.dataclass

import com.google.gson.annotations.SerializedName

data class BookClubDetail(
    val nickname: String = "",
    val profileUrl: String = "",
    val members: List<BookClubDetailMember> = emptyList()
) {
    data class BookClubDetailMember(
        val id: Int = 0,
        val nickname: String = "",
        val profileUrl: String = ""
    )
}





