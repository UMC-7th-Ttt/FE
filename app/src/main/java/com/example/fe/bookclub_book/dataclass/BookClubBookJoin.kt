package com.example.fe.bookclub_book.dataclass

import com.google.gson.annotations.SerializedName

data class BookClubBookJoin(
    @SerializedName(value = "id") val id: Int = 0,
    @SerializedName(value = "bookClubId") val bookClubId: Int = 0
)
