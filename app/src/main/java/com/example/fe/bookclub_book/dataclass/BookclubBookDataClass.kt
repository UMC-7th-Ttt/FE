package com.example.fe.bookclub_book.dataclass

import com.google.gson.annotations.SerializedName

data class BookclubByMonth(
    val month:String = "",
    val coverImg:Int? = null
)

data class BookclubMember(
    val name:String = "",
    val coverImg:Int? = null
)

data class BookclubParticipation(
    val book:String = "",
    val progress: Int
)

data class BookclubDetailMember(
    val name:String = "",
    val coverImg:Int? = null
)
