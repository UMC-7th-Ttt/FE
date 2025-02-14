package com.example.fe.bookclub_book.dataclass

import com.google.gson.annotations.SerializedName

data class BooClubDetailMember(
    @SerializedName(value = "id") val id: Int = 0,
    @SerializedName(value = "nickname") val nickname: String = "",
    @SerializedName(value = "profileUrl") val profileUrl: String = ""
)